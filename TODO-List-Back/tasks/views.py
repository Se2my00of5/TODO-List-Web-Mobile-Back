from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import action
from .models import Task
from drf_yasg import openapi
from .serializers import TaskSerializer
from drf_yasg.utils import swagger_auto_schema

from django.http import JsonResponse
def my_view(request):
    response = JsonResponse({"message": "Hello, world!"})
    response["Access-Control-Allow-Origin"] = "*"  # Разрешает доступ с любого домена
    return response


class TaskViewSet(viewsets.ModelViewSet):
    queryset = Task.objects.all()
    serializer_class = TaskSerializer

    @swagger_auto_schema(
        request_body=openapi.Schema(
            type=openapi.TYPE_ARRAY,
            items=openapi.Schema(
                type=openapi.TYPE_OBJECT,
                properties={
                    'description': openapi.Schema(type=openapi.TYPE_STRING, description='Описание задачи'),
                    'completed': openapi.Schema(type=openapi.TYPE_BOOLEAN, description='Статус задачи'),
                },
                required=['description', 'completed'],
            ),
        ),
        responses={201: TaskSerializer(many=True)}
    )
    # Метод для загрузки нового списка дел
    @action(detail=False, methods=['post'])
    def load_tasks(self, request):
        serializer = TaskSerializer(data=request.data, many=True)
        if serializer.is_valid():
            Task.objects.all().delete()
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @swagger_auto_schema(
        request_body=openapi.Schema(
            type=openapi.TYPE_ARRAY,
            items=openapi.Schema(
                type=openapi.TYPE_OBJECT,
                properties={
                    'description': openapi.Schema(type=openapi.TYPE_STRING, description='Описание задачи'),
                    'completed': openapi.Schema(type=openapi.TYPE_BOOLEAN, description='Статус задачи'),
                },
                required=['description', 'completed'],
            ),
        ),
        responses={201: TaskSerializer(many=True)}
    )
    def create(self, request, *args, **kwargs):
        serializer = TaskSerializer(data=request.data, many=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


