from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework import status
from .models import User, Event, Goal, TimeRestriction
from .serializers import UserSerializer, EventSerializer, GoalSerializer, TimeRestrictionSerializer

# HTTP POST request
# http://127.0.0.1:8000/AddUser/
'''
  Adds user to the database.
  The Id column is auto created in the database, no need to add to JSON body.
  e.g. request:
    {
      "Username": "test",
      "Email": "test@gmail.com",
      "Passwd": "idk", <----- make sure to hash this before hitting the view DO NOT PUT IN PLAIN TEXT
      "Gender": "none",
      "FirstName": "test",
      "LastName": "person"
    }
'''
@csrf_exempt
def AddUser(request):
    if request.method == 'POST':
        userData = JSONParser().parse(request)
        user = UserSerializer(data=userData)
        if user.is_valid():
            user.save()
            return HttpResponse(status=status.HTTP_201_CREATED)
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetUser/<username>/
# Returns a single User object.
# username: the Username of the User
@csrf_exempt
def GetUser(request, username):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        userData = UserSerializer(user)
        return JsonResponse(userData.data)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP DELETE request
# http://127.0.0.1:8000/DeleteUser/<username>/
# Deletes a User from the database.
# username: the Username of the User
@csrf_exempt
def DeleteUser(request, username):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'DELETE':
        User.objects.filter(Id=user.Id).delete()
        return HttpResponse(status=status.HTTP_200_OK)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetAllUsers/
# Returns a list of all User objects.
@csrf_exempt
def GetAllUsers(request):
    if request.method == 'GET':
        users = User.objects.all().distinct()
        usersData = UserSerializer(users, many=True)
        return JsonResponse(usersData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP POST request
# http://127.0.0.1/AddEvent/
'''
  Adds event to the database.
  The Id column is auto created, no need to add it to JSON body.
  e.g. request:
    {
      "Name": "test event",
      "StartDate": "2019-01-01T13:00:00",
      "EndDate": "2019-01-01T14:00:00",
      "NotificationDate": "2019-01-01T12:45:00",
      "IsGoal": false,
      "Frequency": "weekly",
      "UserId": 1
    }
'''
@csrf_exempt
def AddEvent(request):
    if request.method == 'POST':
        eventData = JSONParser().parse(request)
        event = EventSerializer(data=eventData)
        if event.is_valid():
            event.save()
            return HttpResponse(status=status.HTTP_201_CREATED)
        else:
            return  HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetEventsInTimeRange/<start>/<end>/
# Returns a list of Event objects within the time range.
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
@csrf_exempt
def GetEventsInTimeRange(request, start, end):
    try:
        events = Event.objects.filter(StartDate__gte=start, StartDate__lt=end).distinct()
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetEventsInTimeRangeForUser/<username>/<start>/<end>/
# Returns a list of Event objects for a User within a time range.
# username: the Username of the User
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
@csrf_exempt
def GetEventsInTimeRangeForUser(request, username, start, end):
    try:
        user = User.objects.get(Username=username)
        events = Event.objects.filter(StartDate__gte=start, StartDate__lt=end, UserId=user.Id).distinct()
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Event.DoesNotExist:
        return  HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetAllEvents/
# Returns a list of all Event objects.
@csrf_exempt
def GetAllEvents(request):
    if request.method == 'GET':
        events = Event.objects.all().distinct()
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP DELETE request
# http://127.0.0.1:8000/DeleteEventForUser/<username>/<name>/<start>/<end>/
# Deletes a specific Event for a given User.
# username: the Username of the User
# name: the Name of the Event
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
@csrf_exempt
def DeleteEventForUser(request, username, name, start, end):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    try:
        event = Event.objects.get(Name=name, StartDate=start, EndDate=end, UserId=user.Id)
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'DELETE':
        Event.objects.filter(Name=name, StartDate=start, EndDate=end, UserId=user.Id).delete()
        return HttpResponse(status=status.HTTP_200_OK)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def AddGoal(request):
    if request.method == 'POST':
        goalData = JSONParser().parse(request)
        goal = GoalSerializer(data=goalData)
        if goal.is_valid():
            goal.save()
            return HttpResponse(status=status.HTTP_201_CREATED)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP POST request
# http://127.0.0.1:8000/AddTimeRestriction/
'''
  Adds a TimeRestriction for a User.
  The Id column is auto created.
  e.g. request
  {
  "StartDate": "2019-01-01T13:00:00",
  "EndDate": "2019-01-01T15:00:00",
  "Frequency": "weekly",
  "UserId": 1
  }
'''
@csrf_exempt
def AddTimeRestriction(request):
    if request.method == 'POST':
        timeRestrictionData = JSONParser().parse(request)
        timeRestriction = TimeRestrictionSerializer(data=timeRestrictionData)
        if timeRestriction.is_valid():
            timeRestriction.save()
            return HttpResponse(status=status.HTTP_201_CREATED)
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP DELETE request
# http://127.0.0.1:8000/<username>/<start>/<end>/<frequency>/
# Deletes a specific TimeRestriction for a User.
# username: the Username of the User
# start: DateTime type represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime type represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
# frequency: how often the restriction repeats
@csrf_exempt
def DeleteTimeRestrictionForUser(request, username, start, end, frequency):
    try:
        user = User.objects.get(Username=username)
        timeRestriction = TimeRestriction.objects.filter(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'DELETE':
        TimeRestriction.objects.filter(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id).delete()
        return HttpResponse(status=status.HTTP_200_OK)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetTimeRestrictionForUser/<username>/<start>/<end>/<frequency>/
# gets a specific TimeRestriction for a User.
# username: the Username of the User
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
# frequency: how often the restriction repeats
@csrf_exempt
def GetTimeRestrictionForUser(request, username, start, end, frequency):
    try:
        user = User.objects.get(Username=username)
        timeRestriction = TimeRestriction.objects.get(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        timeRestrictionData = TimeRestrictionSerializer(timeRestriction)
        return JsonResponse(timeRestrictionData.data)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/<username>/<start>/<end>/
# Returns a list of TimeRestriction objects inside a specific time range for a User.
# username: the Username of the User
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00) this field is inclusive
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00) this field is exclusive
@csrf_exempt
def GetTimeRestrictionsInTimeRangeForUser(request, username, start, end):
    try:
        user = User.objects.get(Username=username)
        timeRestrictions = TimeRestriction.objects.filter(StartDate__gte=start, EndDate__lt=end, UserId=user.Id).distinct()
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
        return JsonResponse(timeRestrictionsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/<username>/<start>/<end>/
'''
  Gets a list of TimeRestriction objects that have StartDate=start and EndDate=end.
  This method is similar to GetTimeRestrictionsInTimeRangeForUser, except that it uses this logic:
    (StartDate == start) and (EndDate == end) and (UserId == user.Id)
  Meanwhile, GetTimeRestrictionsInTimeRangeForUser uses this logic:
    (StartDate >= start) and (EndDate < end) and (UserId == user.Id)
'''
# username: the Username of the User
# start: DateTime represented as a string (e.g. 2019-01-01T13:00:00)
# end: DateTime represented as a string (e.g. 2019-01-01T15:00:00)
@csrf_exempt
def GetTimeRestrictionsWithStartAndEndForUser(request, username, start, end):
    try:
        user = User.objects.get(Username=username)
        timeRestrictions = TimeRestriction.objects.filter(StartDate=start, EndDate=end, UserId=user.Id).distinct()
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
        return JsonResponse(timeRestrictionsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/<username>/<frequency>/
# Returns a list of TimeRestriction objects for a User that have a specific Frequency.
# username: the Username of the User
# frequency: how often the time restriction repeats
@csrf_exempt
def GetTimeRestrictionsWithFrequencyForUser(request, username, frequency):
    try:
        user = User.objects.get(Username=username)
        timeRestrictions = TimeRestriction.objects.filter(Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
        return JsonResponse(timeRestrictionsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP GET request
# http://127.0.0.1:8000/GetAllTimeRestrictionsForUser/<username>/
# Gets a list of TimeRestrictions for a User.
# username: the Username of the User
@csrf_exempt
def GetAllTimeRestrictionsForUser(request, username):
    try:
        user = User.objects.get(Username=username)
        timeRestrictions = TimeRestriction.objects.filter(UserId=user.Id).distinct()
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        timeRestictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
        return JsonResponse(timeRestictionsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
