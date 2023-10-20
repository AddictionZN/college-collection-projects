namespace Shop.Datalayer.SQL.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Basket : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.BasketItems",
                c => new
                    {
                        ID = c.String(nullable: false, maxLength: 128),
                        BasketID = c.String(maxLength: 128),
                        ProductID = c.String(),
                        Quantity = c.Int(nullable: false),
                        CreatedAt = c.DateTimeOffset(nullable: false, precision: 7),
                    })
                .PrimaryKey(t => t.ID)
                .ForeignKey("dbo.Baskets", t => t.BasketID)
                .Index(t => t.BasketID);
            
            CreateTable(
                "dbo.Baskets",
                c => new
                    {
                        ID = c.String(nullable: false, maxLength: 128),
                        CreatedAt = c.DateTimeOffset(nullable: false, precision: 7),
                    })
                .PrimaryKey(t => t.ID);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.BasketItems", "BasketID", "dbo.Baskets");
            DropIndex("dbo.BasketItems", new[] { "BasketID" });
            DropTable("dbo.Baskets");
            DropTable("dbo.BasketItems");
        }
    }
}
