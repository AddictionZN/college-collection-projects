namespace Shop.Datalayer.SQL.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Customer : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Customers",
                c => new
                    {
                        ID = c.String(nullable: false, maxLength: 128),
                        UserID = c.String(),
                        FirstName = c.String(),
                        LastName = c.String(),
                        Email = c.String(),
                        City = c.String(),
                        ZipCode = c.String(),
                        CreatedAt = c.DateTimeOffset(nullable: false, precision: 7),
                    })
                .PrimaryKey(t => t.ID);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.Customers");
        }
    }
}
