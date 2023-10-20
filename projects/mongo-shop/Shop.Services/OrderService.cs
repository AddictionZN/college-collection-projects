using Shop.Core.Contracts;
using Shop.Core.Models;
using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Services
{
    public class OrderService : IOrderService
    {
        IRepository<Order> orderContext;

        public OrderService(IRepository<Order> OrderContext)
        {
            this.orderContext = OrderContext;
        }

        public void CreateOrder(Order baseOrder, List<BasketItemViewModel> basketItems)
        {
            //The base order contains the user information such as address
            //so now we need to copy across all the product detaisl from the basket into the order.

            foreach (var item in basketItems)
            {
                baseOrder.orderItems.Add(new OrderItem()
                {
                    ProductID = item.ID,
                    ProductName = item.ProductName,
                    Image = item.Image,
                    Price = item.Price,
                    Quantity = item.Quantity
                });
            }
            orderContext.Insert(baseOrder);
            orderContext.Commit();
        }

        // Get the Order List

        public List<Order> GetOrderList()
        {
            return orderContext.Collection().ToList();
        }

        // Get the Specific order need to find the specific value in the find method
        public Order GetOrder(string ID)
        {
            return orderContext.Find(ID);
        }

        public void UpdateOrder(Order updatedOrder)
        {
            orderContext.Update(updatedOrder);
            orderContext.Commit();
        }
    }
}