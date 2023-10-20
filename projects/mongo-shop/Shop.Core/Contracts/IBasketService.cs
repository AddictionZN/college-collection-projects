using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace Shop.Core.Contracts
{
    // This is the interface for the basket in order:
    // 1. Add
    // 2. Remove
    // 3. List the items been selected.
    // 4. Clear the basket

    public interface IBasketService
    {
        void AddToBasket(HttpContextBase httpContext, string productID);
        void RemoveFromBasket(HttpContextBase httpContext, string itemID);
        List<BasketItemViewModel> GetBasketItems(HttpContextBase httpContext);
        BasketSummaryViewModel GetBasketSummary(HttpContextBase httpContext);
        void ClearBasket(HttpContextBase httpContext);
    }
}