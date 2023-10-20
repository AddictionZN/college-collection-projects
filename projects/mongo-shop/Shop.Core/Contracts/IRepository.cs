using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Core.Contracts
{
    // This class needs to be a generic collection for the items 
    // The Reposity System is required for the base entity and the entity framework to detect the main functions:
    // 1. List
    // 2. Commit
    // 3. Delete
    // 4. Find
    // 5. Add
    // 6. Update


    public interface IRepository<T> where T : BaseEntity
    {
        IQueryable<T> Collection();
        void Commit();
        void Delete(string Id);
        T Find(string Id);
        void Insert(T t);
        void Update(T t);
    }
}