using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{

    public class Product : BaseEntity
    {
        [StringLength(20)]
        [DisplayName("Product Name")]
        public string Name { get; set; }

        [DisplayName("Product Description")]
        public string Description { get; set; }

        [Range(0, 100000)]
        [DisplayName("Product Price")]
        public decimal Price { get; set; }
        
        public string Category { get; set; }
        public string Image { get; set; }

    }
}
