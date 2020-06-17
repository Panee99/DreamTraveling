USE master;
GO

CREATE DATABASE DreamTraveling;
GO

USE DreamTraveling;
GO

CREATE TABLE tblUser
    (
      username VARCHAR(20) PRIMARY KEY ,
      password VARCHAR(64) NOT NULL ,
      name NVARCHAR(30) NOT NULL ,
      role VARCHAR(10) NOT NULL ,
      status VARCHAR(10) NOT NULL
    );
GO

CREATE TABLE tblTour
    (
      id INT IDENTITY(1, 1)
             PRIMARY KEY ,
      name NVARCHAR(20) NOT NULL ,
      review NTEXT NOT NULL ,
      fromDate DATE NOT NULL ,
      toDate DATE NOT NULL ,
      price INT NOT NULL ,
      quantity INT NOT NULL ,
      image VARCHAR(255) NOT NULL ,
      dateImport DATE NOT NULL ,
      status VARCHAR(10) NOT NULL
    );
GO

CREATE TABLE tblBooking
    (
      id INT IDENTITY(1, 1)
             PRIMARY KEY ,
      userId VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES dbo.tblUser(username),
      dateOrder DATE NOT NULL ,
      disCountCode VARCHAR(20),
      totalPrice FLOAT NOT NULL ,
      status VARCHAR(10) NOT NULL
    );
GO

CREATE TABLE tblBookingDetails
    (
      bookingId INT FOREIGN KEY REFERENCES dbo.tblBooking ( id ) ,
      tourId INT FOREIGN KEY REFERENCES dbo.tblTour ( id ) ,
      amount INT NOT NULL,
    );
GO

CREATE TABLE tblDiscount
    (
      code VARCHAR(20) PRIMARY KEY ,
      details NVARCHAR(255) ,
      expiryDate DATETIME NOT NULL ,
      status VARCHAR(10) NOT NULL,
    );
GO

CREATE TABLE tblUserUsedDiscount
    (
      userId VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES dbo.tblUser(username) ,
      discountCode VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES dbo.tblDiscount(code),
      UNIQUE ( userId, discountCode )
    );
GO

-- foreign key
ALTER TABLE dbo.tblBooking ADD FOREIGN KEY (disCountCode) REFERENCES dbo.tblDiscount(code)


GO

-- INSERT admin
INSERT INTO dbo.tblUser
        ( username ,
          password ,
          name ,
          role ,
          status
        )
VALUES  ( 'admin' , -- username - varchar(20)
          '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b' , -- password - varchar(64)
          N'Siêu admin' , -- name - nvarchar(30)
          'admin' , -- role - varchar(10)
          'active'  -- status - varchar(10)
        )

-- PROC
---- Tour
CREATE PROC [dbo].[AddTour]
    @name AS NVARCHAR(20) ,
    @review AS NTEXT ,
    @price AS INT ,
    @quantity AS INT ,
    @image AS VARCHAR(255) ,
    @fromDate AS DATE ,
    @toDate AS DATE
AS
    BEGIN
        INSERT  INTO dbo.tblTour
                ( name ,
                  review ,
                  fromDate ,
                  toDate ,
                  price ,
                  quantity ,
                  image ,
                  dateImport ,
                  status
	            )
        VALUES  ( @name , -- name - nvarchar(20)
                  @review , -- review - ntext
                  @fromDate , -- fromDate - datetime
                  @toDate , -- toDate - datetime
                  @price , -- price - int
                  @quantity , -- quantity - int
                  @image , -- image - varchar(255)
                  GETDATE() , -- dateImport - datetime
                  'active'  -- status - varchar(10)
	            );
    END;
GO

---- User
CREATE PROC [dbo].[CheckLogin]
    @username AS VARCHAR(20) ,
    @password AS VARCHAR(64)
AS
    BEGIN
        SELECT  username ,
                password ,
                name ,
                role
        FROM    dbo.tblUser
        WHERE   username = @username
                AND password = @password
                AND status = 'active';
    END;

GO

CREATE PROC [dbo].[CreateUser]
    @username AS VARCHAR(20) ,
    @password AS VARCHAR(64) ,
    @name AS NVARCHAR(30)
AS
    BEGIN
        INSERT  INTO dbo.tblUser
                ( username ,
                  password ,
                  name ,
                  role ,
                  status
	            )
        VALUES  ( @username , -- username - varchar(20)
                  @password , -- password - varchar(64)
                  @name , -- name - nvarchar(30)
                  'user' , -- role - varchar(10)
                  'active'  -- status - varchar(10)
	            );
    END;

GO

EXEC dbo.CreateUser @username = 'user', -- varchar(20)
    @password = '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', -- varchar(64)
    @name = N'Nguyễn Minh Hoàng' -- nvarchar(30)
GO 
    
CREATE PROC [dbo].[GetToursInfoForHome]
    @name AS NVARCHAR(20) ,
    @fromDate AS DATE ,
    @toDate AS DATE ,
    @fromPrice AS INT ,
    @toPrice AS INT ,
    @page AS INT ,
    @rpp AS INT
AS
    BEGIN
		-- get data of result
        SELECT  id ,
                name ,
                review ,
                fromDate ,
                toDate ,
                price ,
                quantity ,
                image
        FROM    dbo.tblTour
        WHERE   status = 'active'
                AND ( @name IS NULL
                      OR name LIKE N'%' + @name + '%'
                    )
                AND ( @fromDate IS NULL
                      OR fromDate <= @fromDate
                      AND toDate >= @toDate
                    )
                AND ( @fromPrice IS NULL
                      OR price >= @fromPrice
                      AND price <= @toPrice
                    )
        ORDER BY dateImport
                OFFSET ( ( @page - 1 ) * @rpp ) ROWS FETCH NEXT @rpp ROW ONLY;
    END;
GO

/*
EXEC dbo.AddTour @name = N'Test1', -- nvarchar(20)
    @review = N'Review Test 1', -- ntext
    @price = 5000000, -- int
    @quantity = 10, -- int
    @image = 'https://loremflickr.com/cache/resized/582_23430110970_821b461680_320_240_nofilter.jpg', -- varchar(255)
    @fromDate = '2020-06-14', -- date
    @toDate = '2020-06-24';
 -- date
GO
EXEC dbo.AddTour @name = N'Test2', -- nvarchar(20)
    @review = N'Review Test 2', -- ntext
    @price = 7000000, -- int
    @quantity = 10, -- int
    @image = 'https://loremflickr.com/cache/resized/65535_49927120598_d9de86b89a_320_240_nofilter.jpg', -- varchar(255)
    @fromDate = '2020-06-14', -- date
    @toDate = '2020-06-24';
 -- date
GO
*/

CREATE PROC [dbo].[GetToursInfoForHomeLength]
    @name AS NVARCHAR(20) ,
    @fromDate AS DATE ,
    @toDate AS DATE ,
    @fromPrice AS INT ,
    @toPrice AS INT
AS
    BEGIN
	-- get number of result
        SELECT  COUNT(id) AS length
        FROM    dbo.tblTour
        WHERE   status = 'active'
                AND ( @name IS NULL
                      OR name LIKE N'%' + @name + '%'
                    )
                AND ( @fromDate IS NULL
                      OR fromDate <= @fromDate
                      AND toDate >= @toDate
                    )
                AND ( @fromPrice IS NULL
                      OR price >= @fromPrice
                      AND price <= @toPrice
                    )
        GROUP BY id;
    END;
GO

