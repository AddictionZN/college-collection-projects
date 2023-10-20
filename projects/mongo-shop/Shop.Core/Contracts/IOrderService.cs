using Shop.Core.Models;
using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Contracts
{
    // The IOrderService is to create a list of the products and show the order.
    // - Create the order
    // - Get the Order list created
    // - Get the Order.
    // - Update the Order

    public interface IOrderService
    {
        void CreateOrder(Order baseOrder, List<BasketItemViewModel> basketItems);
        List<Order> GetOrderList();
        Order GetOrder(string ID);
        void UpdateOrder(Order updatedOrder);
    }
}