using Shop.Core.Contracts;
using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MongoShop.Controllers
{
    public class BasketController : Controller
    {
        IBasketService basketService;
        IOrderService orderService;
        IRepository<Customer> customers;

        public BasketController(IBasketService BasketService, IOrderService OrderService, IRepository<Customer> Customers)
        {
            this.basketService = BasketService;
            this.orderService = OrderService;
            this.customers = Customers;
        }
        // GET: Basket
        public ActionResult Index()
        {
            var model = basketService.GetBasketItems(this.HttpContext);

            return View(model);
        }

        public ActionResult AddToBasket(string ID)
        {
            basketService.AddToBasket(this.HttpContext, ID);//always add one to the basket

            return RedirectToAction("Index");
        }

        public ActionResult RemoveFromBasket(string ID)
        {
            basketService.RemoveFromBasket(this.HttpContext, ID);//always add one to the basket

            return RedirectToAction("Index");
        }

        [Authorize]
        public ActionResult Checkout()
        {
            Customer customer = customers.Collection().FirstOrDefault(c => c.Email == User.Identity.Name);
            if (customer != null)
            {
                Order order = new Order()
                {
                    Email = customer.Email,
                    FirstName = customer.FirstName,
                    LastName = customer.LastName,
                    City = customer.City,
                    ZipCode = customer.ZipCode
                };

                return View(order);
            }
            return RedirectToAction("Error");
        }

        [HttpPost]
        [Authorize]
        public ActionResult Checkout(Order order)
        {
            var basketItems = basketService.GetBasketItems(this.HttpContext);
            order.OrderStatus = "Order Created";
            order.Email = User.Identity.Name;

            //process payment - this could either be a redirect to another view, or a two way process
            //using something like paypal.
            //for simplicity, and because payment processing itself can vary wildy depedant on what payment mechanism you
            //us we'll pretend payment was successful.
            order.OrderStatus = "Payment Processed";
            orderService.CreateOrder(order, basketItems);
            basketService.ClearBasket(this.HttpContext);

            return RedirectToAction("ThankYou", new { OrderID = order.ID });
        }

        public ActionResult ThankYou(string OrderID)
        {
            ViewBag.OrderID = OrderID;
            return View();
        }
        
        public PartialViewResult BasketSummary()
        {
            var basketSummary = basketService.GetBasketSummary(this.HttpContext);
            return PartialView("BasketSummary", basketSummary);
        }
    }
}