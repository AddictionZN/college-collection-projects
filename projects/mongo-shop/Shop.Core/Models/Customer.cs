using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    // The Customer Model needs to have enough details gathered for transferring payment
    // In The task doesn't require payment options so for prototype sakes
    // 


    public class Customer : BaseEntity
    {
        public string UserID { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        //public string Street { get; set; }
        public string City { get; set; }
        //public string State { get; set; }
        public string ZipCode { get; set; }

    }
}