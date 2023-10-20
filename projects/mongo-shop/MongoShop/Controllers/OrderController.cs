using Shop.Core.Contracts;
using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MongoShop.Controllers
{
    public class OrderController : Controller
    {
        IOrderService orderService;

        public OrderController(IOrderService OrderService)
        {
            this.orderService = OrderService;
        }

        // GET: OrderManager
        public ActionResult Index()
        {
            List<Order> orders = orderService.GetOrderList();
            return View(orders);
        }

        public ActionResult UpdateOrder(string ID)
        {
            ViewBag.Statuses = new List<string>() {
                "Order Created",
                "Payment Processed",
                "Order Shipped",
                "Order Complete"
            };
            Order order = orderService.GetOrder(ID);
            return View(order);
        }

        [HttpPost]
        public ActionResult UpdateOrder(Order updatedOrder, string ID)
        {
            //get the original order
            Order order = orderService.GetOrder(ID);

            //update it with whatever we want to update it with
            order.OrderStatus = updatedOrder.OrderStatus;
            orderService.UpdateOrder(order);

            return RedirectToAction("Index");
        }
    }
}