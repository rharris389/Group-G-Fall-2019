from django.db import models

class User(models.Model):
    Id = models.AutoField(primary_key=True)
    Username = models.CharField(max_length=50, unique=True)
    Email = models.CharField(max_length=50, unique=True)
    Passwd = models.CharField(max_length=225)
    Gender = models.CharField(max_length=15, null=True)
    FirstName = models.CharField(max_length=50)
    LastName = models.CharField(max_length=50)

    def __str__(self):
        return f'{self.FirstName} {self.LastName}'

class Event(models.Model):
    Id = models.AutoField(primary_key=True)
    Name = models.CharField(max_length=50)
    StartDate = models.DateTimeField()
    EndDate = models.DateTimeField(null=True)
    NotificationDate = models.DateTimeField(null=True)
    IsGoal = models.BooleanField()
    Frequency = models.CharField(max_length=15)
    UserId = models.ForeignKey(User, on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.Name}'

class TimeRestriction(models.Model):
    Id = models.AutoField(primary_key=True)
    StartDate = models.DateTimeField()
    EndDate = models.DateTimeField(null=True)
    Frequency = models.CharField(max_length=15)
    UserId = models.ForeignKey(User, on_delete=models.CASCADE)

    def __str__(self):
        return f'Start: {self.StartDate} End: {self.EndDate}'
