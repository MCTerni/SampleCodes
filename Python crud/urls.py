from django.urls import path
from .views import initial_records, create_record, update_record, delete_record, RecordsListView, record_chart_view

urlpatterns = [
    path('', initial_records, name='initial_records'),
    path('list', RecordsListView.as_view(), name='list_records'),
    path('new', create_record, name='create_record'),
    path('update/<int:id>/', update_record, name='update_record'),
    path('delete/<int:id>/', delete_record, name='delete_record'),
    path('chart', record_chart_view, name='chart'),
   
]