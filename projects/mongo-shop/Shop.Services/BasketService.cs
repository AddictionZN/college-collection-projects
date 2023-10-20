using Shop.Core.Contracts;
using Shop.Core.Models;
using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace Shop.Services
{
    public class BasketService : IBasketService
    {
        IRepository<Product> productContext;
        IRepository<Basket> basketContext;
        public const string BasketSessionName = "Mongo_Basket";

        public BasketService(IRepository<Product> ProductContext, IRepository<Basket> BasketContext)
        {
            this.productContext = ProductContext;
            this.basketContext = BasketContext;

        }

        private Basket CreateNewBasket(HttpContextBase httpContext)
        {
            Basket basket = new Basket();
            basketContext.Insert(basket);
            basketContext.Commit();

            HttpCookie cookie = new HttpCookie(BasketSessionName);
            cookie.Value = basket.ID;
            cookie.Expires = DateTime.Now.AddDays(1);
            httpContext.Response.Cookies.Add(cookie);

            return basket;
        }

        public void AddToBasket(HttpContextBase httpContext, string productID)
        {
            Basket basket = GetBasket(httpContext, true);
            BasketItem item = basket.BasketItems.FirstOrDefault(i => i.ProductID == productID);

            if (item == null)
            {
                item = new BasketItem()
                {
                    BasketID = basket.ID,
                    ProductID = productID,
                    Quantity = 1
                };
                basket.BasketItems.Add(item);
            }
            else
            {
                item.Quantity = item.Quantity + 1;
            }

            basketContext.Commit();
        }

        public void RemoveFromBasket(HttpContextBase httpContext, string itemID)
        {
            Basket basket = GetBasket(httpContext, true);
            BasketItem item = basket.BasketItems.FirstOrDefault(i => i.ID == itemID);

            if (item != null)
            {
                basket.BasketItems.Remove(item);
                basketContext.Commit();
            }
        }

        public BasketSummaryViewModel GetBasketSummary(HttpContextBase httpContext)
        {

            Basket basket = GetBasket(httpContext, false);
            BasketSummaryViewModel model = new BasketSummaryViewModel(0, 0);
            if (basket != null)
            {
                int? basketCount = (from item in basket.BasketItems
                                    select item.Quantity).Sum();

                decimal? basketTotal = (from item in basket.BasketItems
                                        join p in productContext.Collection() on item.ProductID equals p.ID
                                        select item.Quantity * p.Price).Sum();



                model.BasketCount = basketCount ?? 0;
                model.BasketTotal = basketTotal ?? decimal.Zero;
            }

            return model;
        }

        public List<BasketItemViewModel> GetBasketItems(HttpContextBase httpContext)
        {
            Basket basket = GetBasket(httpContext, false);
            if (basket != null)
            {
                var results = (from b in basket.BasketItems
                               join p in productContext.Collection() on b.ProductID equals p.ID
                               select new BasketItemViewModel()
                               {
                                   ID = b.ID,
                                   Image = p.Image,
                                   Price = p.Price,
                                   ProductName = p.Name,
                                   Quantity = b.Quantity
                               }).ToList();

                return results;
            }
            else
            {
                return new List<BasketItemViewModel>() { }; //return an empty list
            }
        }

        private Basket GetBasket(HttpContextBase httpContext, bool createIfNull)
        {
            HttpCookie cookie = httpContext.Request.Cookies.Get(BasketSessionName);

            Basket basket = new Basket();

            if (cookie != null)
            {
                string basketId = cookie.Value;
                if (!string.IsNullOrEmpty(basketId))
                {
                    basket = basketContext.Find(basketId);
                }
                else
                {
                    if (createIfNull)
                    {
                        basket = CreateNewBasket(httpContext);
                    }
                }
            }
            else
            {
                if (createIfNull)
                {
                    basket = CreateNewBasket(httpContext);
                }
            }

            return basket;
        }

        public void ClearBasket(HttpContextBase httpContext)
        {
            Basket basket = GetBasket(httpContext, false);
            basket.BasketItems.Clear();
            basketContext.Commit();
        }

    }
}