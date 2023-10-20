using Shop.Core.Contracts;
using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Caching;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Datalayer.InMemory
{
    // This class is custom to the Microsoft site for entity framework.
    // I used this class and customized it for my application for the E-Commerce site
    // It follows the entity framework principles:
    // List, Commit, Add, Delete, Find
    
    public class InMemoryRepository<T> : IRepository<T> where T : BaseEntity
    {
        // Need to store the data in the cache so the basket and orders don't dispear when closing the application
        ObjectCache cache = MemoryCache.Default;

        // Need a generic list of the items and the class(for the bootstrap to add)
        List<T> items;
        string className;

        // Generating a constructor for the Memory Repository
        public InMemoryRepository()
        {
            className = typeof(T).Name;

            items = cache[className] as List<T>;
            if (items == null)
            {
                items = new List<T>();
            }
        }

        // Commit once the cache is ready
        public void Commit()
        {
            cache[className] = items;
        }

        // Insert the items into memory
        public void Insert(T t)
        {
            // Add the product to the list
            items.Add(t);
        }

        // Update the items in the memory
        public void Update(T t)
        {
            // First you need to find the product you want to update
            T tToUpdate = items.Find(i => i.ID == t.ID); 

            // If the product isnt null added it to the list
            // Else throw an expection with the class and the ID of the product not found
            if (tToUpdate != null)
            {
                tToUpdate = t;
            }
            else
            {
                throw new Exception(className + " with ID " + t.ID + " Not found!");
            }
        }

        // Find the specific item in the memory
        public T Find(string ID)
        {
            // To find the product in the list and display it.
            // If it doesn't exist throw an expection
            T t = items.Find(i => i.ID == ID);

            if (t != null)
            {
                return t;
            }
            else
            {
                throw new Exception(className + " with Id " + ID + " Not found!");
            }
        }

        // In Case the user needs to query any other statements in the memory
        public IQueryable<T> Collection()
        {
            return items.AsQueryable();
        }

        // To find the item in the list and delete it
        public void Delete(string ID)
        {
            // Find the product with that ID
            T t = items.Find(i => i.ID == ID);

            // If it isnt null, return and delete it from the find method
            // Else if the product doesnt exist throw an error message
            if (t != null)
            {
                items.Remove(t);
            }
            else
            {
                throw new Exception(className + " with Id " + ID + " Not found!");
            }
        }
    }
}

