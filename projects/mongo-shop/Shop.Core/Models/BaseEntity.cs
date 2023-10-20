using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Models
{

    // This is an Abstract class that allows the application to create a specific ID that is encrypted
    // Each Model will have an ID that will link to the entity framework and create a specific table
    // Also will allow a timestamp on the ID and when the entity was created

    public abstract class BaseEntity
    {
        public string ID { get; set; }
        public DateTimeOffset CreatedAt { get; set; }
        public BaseEntity()
        {
            this.ID = Guid.NewGuid().ToString();
            this.CreatedAt = DateTime.Now;
        }
    }
}