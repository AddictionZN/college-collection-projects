using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    public class Basket : BaseEntity
    {
        // This creates a collection for the items in the Basket
        // Will need a constructor for the basket as the items will get generated manually
        // The Basket will get the necessary items to create and add to the basket.
        public virtual ICollection<BasketItem> BasketItems { get; set; }

        public Basket()
        {
            this.BasketItems = new List<BasketItem>();
        }
    }
}