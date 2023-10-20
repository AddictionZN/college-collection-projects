using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.ViewModels
{
    public class BasketSummaryViewModel
    {
        public int BasketCount { get; set; }
        public decimal BasketTotal { get; set; }

        // Need a empty constructor for it to fill the basket with the items
        public BasketSummaryViewModel()
        {

        }

        // If the basket has the items in it calculate the basket for that customer
        public BasketSummaryViewModel(int BasketCount, decimal BasketTotal)
        {
            this.BasketCount = BasketCount;
            this.BasketTotal = BasketTotal;
        }

    }
}