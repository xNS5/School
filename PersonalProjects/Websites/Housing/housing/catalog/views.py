from django.shortcuts import render
from django.template import Context, loader


# Create your views here.
def home(request):
    return render(request, 'housing/main.html')
