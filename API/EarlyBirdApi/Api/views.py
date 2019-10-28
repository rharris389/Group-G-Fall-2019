from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework import status
from .models import User, Event, TimeRestriction
from .serializers import UserSerializer, EventSerializer, TimeRestrictionSerializer

# HTTP Post request
# http://127.0.0.1:8000/AddUser/
# adds user to the database
# the Id column is auto created in the database, no need to add to JSON body
# e.g. body
# {
# "Username": "test",
# "Email": "test@gmail.com",
# "Passwd": "idk", <----- make sure to hash this before hitting the view DO NOT PUT IN PLAIN TEXT
# "Gender": "none",
# "FirstName": "test",
# "LastName": "person"
# }
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

# HTTP Get request
# http://127.0.0.1:8000/GetUser/<username>/
# returns a single User object
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

# HTTP Delete
# http://127.0.0.1:8000/DeleteUser/<username>/
# deletes a user from the database
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

# HTTP Get request
# http://127.0.0.1:8000/GetAllUsers/
# returns a list of User objects
@csrf_exempt
def GetAllUsers(request):
    if request.method == 'GET':
        users = User.objects.all().distinct()
        usersData = UserSerializer(users, many=True)
        return JsonResponse(usersData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP Get request
# http://127.0.0.1:8000/GetPasswd/<username>/
# returns the user's password
@csrf_exempt
def GetPasswd(request, username):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        return HttpResponse(user.Passwd)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

# HTTP Post request
# http://127.0.0.1/AddEvent/
# adds event to the database
# the Id column is auto created, no need to add it to JSON body
# e.g. request
# {
# "Name": "test event",
# "StartDate": "2019-01-01T13:00:00",
# "EndDate": "2019-01-01T14:00:00",
# "NotificationDate": "2019-01-01T12:45:00",
# "IsGoal": false,
# "Frequency": "weekly",
# "UserId": 1
# }
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

# HTTP Get request
# http://127.0.0.1:8000/GetEventsInTimeRange/<beginning_time_inclusive>/<ending_time_exclusive>/
# returns a list of Event objects within the time range
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

# HTTP Get
# http://127.0.0.1:8000/GetEventsInTimeRangeForUser/<username>/<beginning_time_inclusive>/<ending_time_exclusive>/
# returns a list of Event objects for a user within a time range
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

# HTTP Get request
# http://127.0.0.1:8000/GetAllEvents/
# returns a list of all Event objects in the database
@csrf_exempt
def GetAllEvents(request):
    if request.method == 'GET':
        events = Event.objects.all().distinct()
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
