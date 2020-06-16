USE master;
GO

CREATE DATABASE DreamTraveling;
GO

USE DreamTraveling;
GO

CREATE TABLE tblUser
    (
      username VARCHAR(20) PRIMARY KEY ,
      password VARBINARY(64) NOT NULL ,
      name NVARCHAR(30) NOT NULL ,
      role VARCHAR(10) NOT NULL
    );
GO

CREATE TABLE tblTour
    (
      id INT IDENTITY(1, 1)
             PRIMARY KEY ,
      name NVARCHAR(20) NOT NULL ,
	  review NTEXT NOT NULL,
      fromDate DATETIME NOT NULL ,
      toDate DATETIME NOT NULL ,
      price FLOAT NOT NULL ,
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
      userId VARCHAR(20) NOT NULL ,
      dateOrder DATETIME NOT NULL,
	  disCountCode VARCHAR(20),
	  totalPrice FLOAT NOT NULL
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
      expiryDate DATETIME NOT NULL
    );
GO

CREATE TABLE tblUserUsedDiscount
    (
      userId VARCHAR(20) NOT NULL ,
      discountCode VARCHAR(20) NOT NULL ,
      UNIQUE ( userId, discountCode )
    );
GO
