using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    public class Order : BaseEntity
    {
        public Order()
        {
            this.orderItems = new List<OrderItem>();
        }

        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string City { get; set; }
        public string ZipCode { get; set; }
        public string OrderStatus { get; set; }
        public virtual ICollection<OrderItem> orderItems { get; set; }
    }
}