using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(MongoShop.Startup))]
namespace MongoShop
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
