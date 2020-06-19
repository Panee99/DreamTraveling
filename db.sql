﻿USE master;
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
      dateImport DATETIME NOT NULL ,
      status VARCHAR(10) NOT NULL
    );
GO

CREATE TABLE tblBooking
    (
      id INT IDENTITY(1, 1)
             PRIMARY KEY ,
      userId VARCHAR(20) NOT NULL
                         FOREIGN KEY REFERENCES dbo.tblUser ( username ) ,
      dateOrder DATE NOT NULL ,
      disCountCode VARCHAR(20) ,
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
      userId VARCHAR(20) NOT NULL
                         FOREIGN KEY REFERENCES dbo.tblUser ( username ) ,
      discountCode VARCHAR(20) NOT NULL
                               FOREIGN KEY REFERENCES dbo.tblDiscount ( code ) ,
      UNIQUE ( userId, discountCode )
    );
GO

-- foreign key
ALTER TABLE dbo.tblBooking ADD FOREIGN KEY (disCountCode) REFERENCES dbo.tblDiscount(code);


GO

-- INSERT admin
INSERT  INTO dbo.tblUser
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
        );
		
GO
        
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

ALTER PROC [dbo].[UpdateTour]
    @id AS INT ,
    @name AS NVARCHAR(20) ,
    @review AS NTEXT ,
    @price AS INT ,
    @quantity AS INT ,
    @image AS VARCHAR(255) ,
    @fromDate AS DATE ,
    @toDate AS DATE
AS
    BEGIN
        UPDATE  dbo.tblTour
        SET     name = @name ,
                review = @review ,
                fromDate = @fromDate ,
                toDate = @toDate ,
                price = @price ,
                quantity = @quantity ,
                image = CASE WHEN @image IS NOT NULL THEN @image
                             ELSE image
                        END
        WHERE   id = @id
                AND status = 'active';
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
    @name = N'Nguyễn Minh Hoàng';
 -- nvarchar(30)
GO 
    
CREATE PROC [dbo].[GetToursInfoForHome]
    @name AS NVARCHAR(20) ,
    @fromDate AS DATE ,
    @toDate AS DATE ,
    @fromPrice AS INT ,
    @toPrice AS INT ,
    @minQuantity AS INT ,
    @fromNowOn AS BIT ,
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
                AND quantity >= @minQuantity
                AND ( @fromNowOn = 1
                      OR fromDate >= CAST(CURRENT_TIMESTAMP AS DATE)
                    )
        ORDER BY dateImport DESC
                OFFSET ( ( @page - 1 ) * @rpp ) ROWS FETCH NEXT @rpp ROW ONLY;
    END;
GO

CREATE PROC [dbo].[GetToursInfoForHomeLength]
    @name AS NVARCHAR(20) ,
    @fromDate AS DATE ,
    @toDate AS DATE ,
    @fromPrice AS INT ,
    @toPrice AS INT ,
    @minQuantity AS INT ,
    @fromNowOn AS BIT
AS
    BEGIN
	-- get number of result
        SELECT  COUNT(*) AS length
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
                AND quantity >= @minQuantity
                AND ( @fromNowOn = 1
                      OR fromDate >= CAST(CURRENT_TIMESTAMP AS DATE)
                    );
    END;
GO

CREATE PROC GetTourByID @id AS INT
AS
    BEGIN
        SELECT  id ,
                name ,
                review ,
                fromDate ,
                toDate ,
                price ,
                quantity ,
                image
        FROM    dbo.tblTour
        WHERE   id = @id
                AND status = 'active';
    END;
GO 

CREATE PROC DeactiveTourByID @id AS INT
AS
    BEGIN
        UPDATE  dbo.tblTour
        SET     status = 'inactive'
        WHERE   id = @id;
    END;
GO

CREATE PROC getImageOfTour @id AS INT
AS
    BEGIN
        SELECT  image
        FROM    dbo.tblTour
        WHERE   id = @id;
    END;
GO

SELECT  *
FROM    dbo.tblTour;