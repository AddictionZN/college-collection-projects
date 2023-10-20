using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    public class OrderItem : BaseEntity
    {
        public string OrderID { get; set; }
        public string ProductID { get; set; }
        public string ProductName { get; set; }
        public decimal Price { get; set; }
        public string Image { get; set; }
        public int Quantity { get; set; }
    }
}