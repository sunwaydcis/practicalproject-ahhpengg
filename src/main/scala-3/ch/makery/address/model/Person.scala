package ch.makery.address.model

import scalafx.beans.property.{StringProperty, IntegerProperty, ObjectProperty}
import java.time.LocalDate
import ch.makery.address.util.Database
import ch.makery.address.util.DateUtil._
import scalikejdbc._
import scala.util.{ Try, Success, Failure }

class Person (val firstNameS : String, val lastNameS : String) extends Database :
  def this()     = this(null, null)
  var firstName  = new StringProperty(firstNameS)
  var lastName   = new StringProperty(lastNameS)
  var street     = new StringProperty("some Street")
  var postalCode = IntegerProperty(1234)
  var city       = new StringProperty("some city")
  var date       = ObjectProperty[LocalDate](LocalDate.of(1999, 2, 21))

  def save() : Try[Int] =
    if (!(isExist)) then
      Try(DB autoCommit { implicit session =>
        sql"""
      insert into person (firstName, lastName,
       street, postalCode, city, date) values
       (${firstName.value}, ${lastName.value}, ${street.value},
        ${postalCode.value},${city.value}, ${date.value.asString})
     """.update.apply()
      })
    else
      Try(DB autoCommit { implicit session =>
        sql"""
     update person
     set
     firstName  = ${firstName.value} ,
     lastName   = ${lastName.value},
     street     = ${street.value},
     postalCode = ${postalCode.value},
     city       = ${city.value},
     date       = ${date.value.asString}
      where firstName = ${firstName.value} and
      lastName = ${lastName.value}
     """.update.apply()
      })

  def delete() : Try[Int] =
    if (isExist) then
      Try(DB autoCommit { implicit session =>
        sql"""
     delete from person where
      firstName = ${firstName.value} and lastName = ${lastName.value}
     """.update.apply()
      })
    else
      throw new Exception("Person not Exists in Database")
  def isExist : Boolean =
    DB readOnly { implicit session =>
      sql"""
     select * from person where
     firstName = ${firstName.value} and lastName = ${lastName.value}
    """.map(rs => rs.string("firstName")).single.apply()
    } match
      case Some(x) => true
      case None => false



object Person extends Database:
  def apply (
              firstNameS : String,
              lastNameS : String,
              streetS : String,
              postalCodeI : Int,
              cityS : String,
              dateS : String
            ) : Person =

    new Person(firstNameS, lastNameS) :
      street.value     = streetS
      postalCode.value = postalCodeI
      city.value       = cityS
      date.value       = dateS.parseLocalDate.getOrElse(null)


  def initializeTable() =
    DB autoCommit { implicit session =>
      sql"""
    create table person (
      id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
      firstName varchar(64),
      lastName varchar(64),
      street varchar(200),
      postalCode int,
      city varchar(100),
      date varchar(64)
    )
    """.execute.apply()
    }

  def getAllPersons : List[Person] =
    DB readOnly { implicit session =>
      sql"select * from person".map(rs => Person(rs.string("firstName"),
        rs.string("lastName"),rs.string("street"),
        rs.int("postalCode"),rs.string("city"), rs.string("date") )).list.apply()
    }




