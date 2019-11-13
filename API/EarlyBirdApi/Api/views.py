from django.http import HttpResponse
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from rest_framework import status
from .models import User, Event, Goal, TimeRestriction
from .serializers import UserSerializer, EventSerializer, GoalSerializer, TimeRestrictionSerializer

@csrf_exempt
def AddUser(request):
    if request.method != 'POST':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    userData = JSONParser().parse(request)
    user = UserSerializer(data=userData)
    if user.is_valid():
        user.save()
        return HttpResponse(status=status.HTTP_201_CREATED)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def EditUser(request, username, property, newData):
    if request.method != 'PATCH':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    if property == 'Id' or property == 'Username' or property == 'Passwd':
        return HttpResponse(status=status.HTTP_403_FORBIDDEN)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if property == 'Email':
        user.Email = newData
        user.save()
    elif property == 'Gender':
        user.Gender = newData
        user.save()
    elif property == 'FirstName':
        user.FirstName = newData
        user.save()
    elif property == 'LastName':
        user.LastName = newData
        user.save()
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def EditPasswdForUser(request):
    if request.method != 'PATCH':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    newUserData = JSONParser().parse(request)

    try:
        user = User.objects.get(Username=newUserData['Username'])
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    user.Passwd = newUserData['Passwd']
    user.save()
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def GetUser(request, username):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    userData = UserSerializer(user)
    return JsonResponse(userData.data)

@csrf_exempt
def DeleteUser(request, username):
    if request.method != 'DELETE':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    User.objects.filter(Username=username).delete()
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def GetAllUsers(request):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    users = User.objects.all()
    usersData = UserSerializer(users, many=True)
    return JsonResponse(usersData.data, safe=False)

@csrf_exempt
def AddEvent(request):
    if request.method != 'POST':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    eventData = JSONParser().parse(request)
    event = EventSerializer(data=eventData)
    if event.is_valid():
        event.save()
        return HttpResponse(status=status.HTTP_201_CREATED)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def EditEventForUser(request, username, name, start, end, notification, frequency, property, newData):
    if request.method != 'PATCH':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    if property == 'Id' or property == 'UserId':
        return HttpResponse(status=status.HTTP_403_FORBIDDEN)

    try:
        user = User.objects.get(Username=username)
        event = Event.objects.get(Name=name, StartDate=start, EndDate=end, NotificationDate=notification, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if property == 'Name':
        event.Name = newData
        event.save()
    elif property == 'StartDate':
        event.StartDate = newData
        event.save()
    elif property == 'EndDate':
        event.EndDate = newData
        event.save()
    elif property == 'NotificationDate':
        event.NotificationDate = newData
        event.save()
    elif property == 'Frequency':
        event.Frequency = newData
        event.save()
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def GetEventsInTimeRange(request, start, end):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        events = Event.objects.filter(StartDate__lte=start, EndDate__gt=end).distinct()
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    eventsData = EventSerializer(events, many=True)
    return JsonResponse(eventsData.data, safe=False)

@csrf_exempt
def GetEventsInTimeRangeForUser(request, username, start, end):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
        events = Event.objects.filter(StartDate__lte=start, EndDate__gt=end, UserId=user.Id).distinct()
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    eventsData = EventSerializer(events, many=True)
    return JsonResponse(eventsData.data, safe=False)

@csrf_exempt
def GetAllEvents(request):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    events = Event.objects.all()
    eventsData = EventSerializer(events, many=True)
    return JsonResponse(eventsData.data, safe=False)

@csrf_exempt
def DeleteEventForUser(request, username, name, start, end):
    if request.method != 'DELETE':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
        event = Event.objects.get(Name=name, StartDate=start, EndDate=end, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Event.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    Event.objects.filter(Name=name, StartDate=start, EndDate=end, UserId=user.Id).delete()
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def AddGoal(request):
    if request.method != 'POST':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    goalData = JSONParser().parse(request)
    goal = GoalSerializer(data=goalData)
    if goal.is_valid():
        goal.save()
        return HttpResponse(status=status.HTTP_201_CREATED)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def EditGoalById(request, id, property, newData):
    if request.method != 'PATCH':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    if property == 'Id' or property == 'UserId':
        return HttpResponse(status=status.HTTP_403_FORBIDDEN)

    try:
        goal = Goal.objects.get(Id=id)
    except Goal.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if property == 'Name':
        goal.Name = newData
        goal.save()
    elif property == 'IsCompleted':
        if newData == 'true':
            goal.IsCompleted = True
            goal.save()
        elif newData == 'false':
            goal.IsCompleted = False
            goal.save()
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    elif property == 'Notes':
        goal.Notes = newData
        goal.save()
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def DeleteGoalForUser(request, username, name, isCompleted, notes):
    if request.method != 'DELETE':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
        if isCompleted == 'true':
            goal = Goal.objects.get(Name=name, IsCompleted=True, Notes=notes, UserId=user.Id)
        elif isCompleted == 'false':
            goal = Goal.objects.get(Name=name, IsCompleted=False, Notes=notes, UserId=user.Id)
        else:
            return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except Goal.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    Goal.objects.filter(Name=name, IsCompleted=isCompleted, Notes=notes, UserId=user.Id).delete()
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def GetGoalById(request, id):
    if request.method != 'GET':
        return HttpResponse(status.HTTP_400_BAD_REQUEST)

    try:
        goal = Goal.objects.get(Id=id)
    except Goal.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    goalData = GoalSerializer(goal)
    return JsonResponse(goalData.data)

@csrf_exempt
def GetAllCompletedGoalsForUser(request, username):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    goals = Goal.objects.filter(IsCompleted=True, UserId=user.Id).distinct()
    goalsData = GoalSerializer(goals, many=True)
    return JsonResponse(goalsData.data, safe=False)

@csrf_exempt
def GetAllIncompletedGoalsForUser(request, username):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    goals = Goal.objects.filter(IsCompleted=False, UserId=user.Id).distinct()
    goalsData = GoalSerializer(goals, many=True)
    return JsonResponse(goalsData.data, safe=False)

@csrf_exempt
def AddTimeRestriction(request):
    if request.method != 'POST':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    timeRestrictionData = JSONParser().parse(request)
    timeRestriction = TimeRestrictionSerializer(data=timeRestrictionData)
    if timeRestriction.is_valid():
        timeRestriction.save()
        return HttpResponse(status=status.HTTP_201_CREATED)
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

@csrf_exempt
def EditTimeRestrictionForUser(request, username, start, end, frequency, property, newData):
    if request.method != 'PATCH':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    if property == 'Id' or 'UserId':
        return HttpResponse(status=status.HTTP_403_FORBIDDEN)

    try:
        user = User.objects.get(Username=username)
        timeRestriction = TimeRestriction.objects.get(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    if property == 'StartDate':
        timeRestriction.StartDate = newData
        timeRestriction.save()
    elif property == 'EndDate':
        timeRestriction.EndDate = newData
        timeRestriction.save()
    elif property == 'Frequency':
        timeRestriction.Frequency = newData
        timeRestriction.save()
    else:
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def DeleteTimeRestrictionForUser(request, username, start, end, frequency):
    if request.method != 'DELETE':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
        timeRestriction = TimeRestriction.objects.get(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    TimeRestriction.objects.filter(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id).distinct()
    return HttpResponse(status=status.HTTP_200_OK)

@csrf_exempt
def GetTimeRestrictionForUser(request, username, start, end, frequency):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
        timeRestriction = TimeRestriction.objects.get(StartDate=start, EndDate=end, Frequency=frequency, UserId=user.Id)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)
    except TimeRestriction.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    timeRestrictionData = TimeRestrictionSerializer(timeRestriction)
    return JsonResponse(timeRestrictionData.data)

@csrf_exempt
def GetTimeRestrictionsInTimeRangeForUser(request, username, start, end):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    timeRestrictions = TimeRestriction.objects.filter(StartDate__lte=start, EndDate__gt=end, UserId=user.Id).distinct()
    timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
    return JsonResponse(timeRestrictionsData.data, safe=False)

@csrf_exempt
def GetTimeRestrictionsWithStartAndEndForUser(request, username, start, end):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    timeRestrictions = TimeRestriction.objects.filter(StartDate=start, EndDate=end, UserId=user.Id).distinct()
    timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
    return JsonResponse(timeRestrictionsData.data, safe=False)

@csrf_exempt
def GetTimeRestrictionsWithFrequencyForUser(request, username, frequency):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    timeRestrictions = TimeRestriction.objects.filter(Frequency=frequency, UserId=user.Id).distinct()
    timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
    return JsonResponse(timeRestrictionsData.data, safe=False)

@csrf_exempt
def GetAllTimeRestrictionsForUser(request, username):
    if request.method != 'GET':
        return HttpResponse(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(Username=username)
    except User.DoesNotExist:
        return HttpResponse(status=status.HTTP_404_NOT_FOUND)

    timeRestrictions = TimeRestriction.objects.filter(UserId=user.Id).distinct()
    timeRestrictionsData = TimeRestrictionSerializer(timeRestrictions, many=True)
    return JsonResponse(timeRestrictionsData.data, safe=False)
