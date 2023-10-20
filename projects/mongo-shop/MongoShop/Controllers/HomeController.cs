using Shop.Core.Contracts;
using Shop.Core.Models;
using Shop.Core.ViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MongoShop.Controllers
{
    public class HomeController : Controller
    {
        // Get the repository of the Product and Categorys to get displayed on the index page
        IRepository<Product> context;
        IRepository<ProductCategory> productCategories;

        // Create a constructor to get these Values of the and declare them
        public HomeController(IRepository<Product> productContext, IRepository<ProductCategory> productCategoryContext)
        {
            this.context = productContext;
            this.productCategories = productCategoryContext;
        }

        // To go to the admin page/employee page
        public ActionResult Admin()
        {
            return View();
        }

        // GET: ProductManager
        public ActionResult Index(string Category = null, string Search = null)
        {
            // Get the list of the products and the categories then display them on the home page
            List<Product> products;
            List<ProductCategory> categories = productCategories.Collection().ToList();

            //Category can never be null will always have an ALL filter
            //Getting the search bar to display against the name

            if (Category != null && Search == null)
            {
                products = context.Collection().Where(p => p.Category == Category).ToList();
            }
            else if (Category == null && Search != null)
            {
                products = context.Collection().Where(p => p.Name.StartsWith(Search)).ToList();
            }
            else
            {
                products = context.Collection().ToList();
            }

            // Get the List Models and display the products and categories
            ProductListViewModel model = new ProductListViewModel();
            model.ProductCategories = categories;
            model.Products = products;


            // Return the View of the Model
            return View(model);
        }

        // If the customer clicks on the specific product then go to the details of that product
        public ActionResult Details(string ID)
        {
            // 1. Find the product if not exist throw 404 else return the view of that product
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
    }
}