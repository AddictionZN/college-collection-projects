using Shop.Core.Contracts;
using Shop.Core.Models;
using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MongoShop.Controllers
{
    public class ProductController : Controller
    {

        // Getting the product Repository and the Category repository
        IRepository<Product> context;
        IRepository<ProductCategory> productCategories;

        public ProductController(IRepository<Product> productContext, IRepository<ProductCategory> productCategoryContext)
        {
            this.context = productContext;
            this.productCategories = productCategoryContext;
        }

        // GET: ProductManager
        public ActionResult Index()
        {
            List<Product> products = context.Collection().ToList();
            return View(products);
        }

        #region Create

        public ActionResult Create()
        {
            ProductManagerViewModel viewModel = new ProductManagerViewModel();

            viewModel.Product = new Product();
            viewModel.ProductCategories = productCategories.Collection();
            return View(viewModel);
        }

        [HttpPost]
        public ActionResult Create(Product product, HttpPostedFileBase file)
        {
            if (!ModelState.IsValid)
            {
                return View(product);
            }
            else
            {
                //save the image
                if (file != null)
                {
                    product.Image = product.ID + Path.GetExtension(file.FileName);
                    file.SaveAs(Server.MapPath("//Content//productImages//") + product.Image);
                }

                context.Insert(product);
                context.Commit();

                return RedirectToAction("Index");
            }
        }

        #endregion

        #region Edit

        public ActionResult Edit(string ID)
        {
            Product product = context.Find(ID);
            if (product == null)
            {
                return HttpNotFound();
            }
            else
            {
                ProductManagerViewModel viewModel = new ProductManagerViewModel();

                viewModel.Product = product;
                viewModel.ProductCategories = productCategories.Collection();

                return View(viewModel);
            }
        }

        [HttpPost]
        public ActionResult Edit(Product product, string ID, HttpPostedFileBase file)
        {
            Product productToEdit = context.Find(ID);

            if (productToEdit == null)
            {
                return HttpNotFound();
            }
            else
            {
                if (!ModelState.IsValid)
                {
                    return View(product);
                }

                //save the image
                if (file != null)
                {
                    product.Image = product.ID + Path.GetExtension(file.FileName);
                    file.SaveAs(Server.MapPath("//Content//productImages//") + product.Image);
                }

                productToEdit.Category = product.Category;
                productToEdit.Description = product.Description;
                productToEdit.Name = product.Name;
                productToEdit.Price = product.Price;

                context.Commit();
            }

            return RedirectToAction("Index");
        }
        #endregion

        #region Delete

        public ActionResult Delete(string ID)
        {
            Product product = context.Find(ID);
            if (product == null)
            {
                return HttpNotFound();
            }
            else
            {
                return View(product);
            }
        }

        [HttpPost]
        [ActionName("Delete")]
        public ActionResult ConfirmDelete(string ID)
        {
            Product product = context.Find(ID);
            if (product == null)
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

        #endregion

    }
}