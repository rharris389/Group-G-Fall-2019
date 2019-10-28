from rest_framework import serializers
from .models import User, Event, TimeRestriction

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('Id', 'Username', 'Email', 'Passwd', 'Gender', 'FirstName', 'LastName')

class EventSerializer(serializers.ModelSerializer):
    class Meta:
        model = Event
        fields = ('Id', 'Name', 'StartDate', 'EndDate', 'NotificationDate', 'IsGoal', 'Frequency', 'UserId')

class TimeRestrictionSerializer(serializers.ModelSerializer):
    class Meta:
        model = TimeRestriction
        fields = ('Id', 'StartDate', 'EndDate', 'Frequency', 'UserId')
