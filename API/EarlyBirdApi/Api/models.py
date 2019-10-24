from django.db import models

class User(models.Model):
    Id = models.IntegerField(primary_key=True, auto_created=True)
    Email = models.CharField(max_length=50, unique=True)
    Passwd = models.CharField(max_length=150)
    Gender = models.CharField(max_length=15, null=True)
    FirstName = models.CharField(max_length=50)
    LastName = models.CharField(max_length=50)

class Event(models.Model):
    Id = models.IntegerField(primary_key=True, auto_created=True)
    Name = models.CharField(max_length=50)
    StartDate = models.DateTimeField()
    EndDate = models.DateTimeField(null=True)
    NotificationDate = models.DateTimeField(null=True)
    IsGoal = models.BooleanField()
    Frequency = models.CharField(max_length=15)

class TimeRestriction(models.Model):
    Id = models.IntegerField(primary_key=True, auto_created=True)
    StartDate = models.DateTimeField()
    EndDate = models.DateTimeField(null=True)
    Frequency = models.CharField(max_length=15)

class ConfigurationEvent(models.Model):
    Id = models.IntegerField(primary_key=True, auto_created=True)
    UserId = models.ForeignKey(User, on_delete=models.CASCADE)
    EventId = models.ForeignKey(Event, on_delete=models.CASCADE)

class ConfigurationTimeRestriction(models.Model):
    Id = models.IntegerField(primary_key=True, auto_created=True)
    UserId = models.ForeignKey(User, on_delete=models.CASCADE)
    TimeRestrictionId = models.ForeignKey(TimeRestriction, on_delete=models.CASCADE)
