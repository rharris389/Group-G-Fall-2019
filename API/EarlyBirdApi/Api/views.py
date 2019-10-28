from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework import status
from .models import User, Event, TimeRestriction
from .serializers import UserSerializer, EventSerializer, TimeRestrictionSerializer

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

@csrf_exempt
def GetAllUsers(request):
    if request.method == 'GET':
        users = User.objects.all()
        usersData = UserSerializer(users, many=True)
        return JsonResponse(usersData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

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

@csrf_exempt
def GetEvents(request, start, end):
    try:
        events = Event.objects.filter(StartDate__gte=start, StartDate__lt=end)
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def GetAllEvents(request):
    if request.method == 'GET':
        events = Event.objects.all()
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
