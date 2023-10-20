using Shop.Core.Contracts;
using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Shop.Datalayer.SQL
{
    // This Class is for the DataLayer for entity Framework
    public class SQLRepository<T> : IRepository<T> where T : BaseEntity
    {

        internal DataContext context;
        internal DbSet<T> dbSet;

        public SQLRepository(DataContext Context)
        {
            this.context = Context;
            this.dbSet = context.Set<T>();
        }

        public IQueryable<T> Collection()
        {
            return dbSet;
        }

        public void Commit()
        {
            context.SaveChanges();
        }

        public void Delete(string ID)
        {
            var t = Find(ID);
            if (context.Entry(t).State == EntityState.Detached)
                dbSet.Attach(t);

            dbSet.Remove(t);
        }

        public void Update(T t)
        {
            dbSet.Attach(t);
            context.Entry(t).State = EntityState.Modified;
        }

        public T Find(string ID)
        {
            return dbSet.Find(ID);
        }

        public void Insert(T t)
        {
            dbSet.Add(t);
        }


    }
}
