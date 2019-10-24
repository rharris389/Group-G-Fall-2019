from rest_framework import serializers
from .models import User, Event, TimeRestriction, ConfigurationEvent, ConfigurationTimeRestriction

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('Id', 'Email', 'Passwd', 'Gender', 'FirstName', 'LastName')

class EventSerializer(serializers.ModelSerializer):
    class Meta:
        model = Event
        fields = ('Id', 'Name', 'StartDate', 'EndDate', 'NotificationDate', 'IsGoal', 'Frequency')

class TimeRestrictionSerializer(serializers.ModelSerializer):
    class Meta:
        model = TimeRestriction
        fields = ('Id', 'StartDate', 'EndDate', 'Frequency')

class ConfigurationEventSerializer(serializers.ModelSerializer):
    class Meta:
        model = ConfigurationEvent
        fields = ('Id', 'UserId', 'EventId')

class ConfigurationTimeRestriction(serializers.ModelSerializer):
    class Meta:
        model = ConfigurationTimeRestriction
        fields = ('Id', 'UserId', 'TimeRestrictionId')
