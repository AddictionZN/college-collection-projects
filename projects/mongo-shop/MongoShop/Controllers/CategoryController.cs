using Shop.Core.Contracts;
using Shop.Core.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MongoShop.Controllers
{
    public class CategoryController : Controller
    {
        IRepository<ProductCategory> context;

        public CategoryController(IRepository<ProductCategory> ProductCategoryContext)
        {
            this.context = ProductCategoryContext;
        }


        // GET: ProductManager
        public ActionResult Index()
        {
            List<ProductCategory> productCategories = context.Collection().ToList();
            return View(productCategories);
        }

        public ActionResult Create()
        {
            ProductCategory productCategory = new ProductCategory();
            return View();
        }

        [HttpPost]
        public ActionResult Create(ProductCategory productCategory)
        {
            if (!ModelState.IsValid)
            {
                return View(productCategory);
            }
            else
            {
                context.Insert(productCategory);
                context.Commit();

                return RedirectToAction("Index");
            }
        }

        public ActionResult Edit(string ID)
        {
            ProductCategory productCategory = context.Find(ID);
            if (productCategory == null)
            {
                return HttpNotFound();
            }
            else
            {
                return View(productCategory);
            }
        }

        [HttpPost]
        public ActionResult Edit(ProductCategory productCategory, string ID)
        {
            ProductCategory productCategoryToEdit = context.Find(ID);

            if (productCategoryToEdit == null)
            {
                return HttpNotFound();
            }
            else
            {
                if (!ModelState.IsValid)
                {
                    return View(productCategory);
                }
                productCategoryToEdit.Category = productCategory.Category;

                context.Commit();
            }

            return RedirectToAction("Index");
        }

        public ActionResult Delete(string ID)
        {
            ProductCategory productCategory = context.Find(ID);
            if (productCategory == null)
            {
                return HttpNotFound();
            }
            else
            {
                return View(productCategory);
            }
        }

        [HttpPost]
        [ActionName("Delete")]
        public ActionResult ConfirmDelete(string ID)
        {
            ProductCategory productCategory = context.Find(ID);
            if (productCategory == null)
            {
                return HttpNotFound();
            }
            else
            {
                context.Delete(ID);
                context.Commit();
                return RedirectToAction("Index");
            }

        }
    }
}