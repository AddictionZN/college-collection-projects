using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.ViewModels
{
    // Need to get the product and list the number of the items in that category

    public class ProductManagerViewModel
    {
        public Product Product { get; set; }
        public IEnumerable<ProductCategory> ProductCategories { get; set; }
    }
}
