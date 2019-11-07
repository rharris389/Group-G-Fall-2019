from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework import status
from .models import User, Event, Goal, TimeRestriction
from .serializers import UserSerializer, EventSerializer, GoalSerializer, TimeRestrictionSerializer

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

@csrf_exempt
def GetAllUsers(request):
    if request.method == 'GET':
        users = User.objects.all().distinct()
        usersData = UserSerializer(users, many=True)
        return JsonResponse(usersData.data, safe=False)
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

@csrf_exempt
def GetAllEvents(request):
    if request.method == 'GET':
        events = Event.objects.all().distinct()
        eventsData = EventSerializer(events, many=True)
        return JsonResponse(eventsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def DeleteEventForUser(request, username, name, start, end):
    try:
        user = User.objects.get(Username=username)
        event = Event.objects.get(Name=name, StartDate=start, EndDate=end, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
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

@csrf_exempt
def DeleteGoalForUser(request, username, name, isCompleted, notes):
    try:
        user = User.objects.get(Username=username)
        goal = Goal.objects.get(Name=name, IsCompleted=isCompleted, Notes=notes, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Goal.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'DELETE':
        if isCompleted == 'true':
            Goal.objects.filter(Name=name, IsCompleted=True, Notes=notes, UserId=user.Id).delete()
        else:
            Goal.objects.filter(Name=name, IsCompleted=False, Notes=notes, UserId=user.Id).delete()
        return HttpResponse(status=status.HTTP_200_OK)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def GetAllCompletedGoalsForUser(request, username):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        goals = Goal.objects.filter(IsCompleted=True, UserId=user.Id).distinct()
        goalsData = GoalSerializer(goals, many=True)
        return JsonResponse(goalsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def GetAllIncompletedGoalsForUser(request, username):
    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        goals = Goal.objects.filter(IsCompleted=False, UserId=user.Id)
        goalsData = GoalSerializer(goals, many=True)
        return JsonResponse(goalsData.data, safe=False)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

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
