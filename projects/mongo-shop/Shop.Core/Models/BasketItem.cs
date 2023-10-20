using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    // This Class will need:
    // - ID for the basket to each of the customers
    // - ID for the product with each basket item
    // - Quantity of the Item and that specific item
    // - To follow the polymorphic of the base entity

    public class BasketItem : BaseEntity
    {
        public string BasketID { get; set; }
        public string ProductID { get; set; }
        public int Quantity { get; set; }
    }
}