using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{
    // This Class just need to know which category the items falls under

    public class ProductCategory : BaseEntity
    {
        public string Category { get; set; }
    }
}
