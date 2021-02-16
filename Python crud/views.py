"""
Model for cst8333 project.
Author: Mateus Carnevalli Terni
Version: 20200405

Reference for ListView from Model:
    'django-tables2 Tutorial'
    [Online]. Available: https://django-tables2.readthedocs.io/en/latest/pages/tutorial.html
    [Accessed: 19-Mar-2020]

"""
from django_tables2 import SingleTableView
from django.shortcuts import render, redirect
from .models import Records
from .forms import RecordForm
from .iofile import IOF
from .tables import RecordsTable
from django.db.models import Sum
from chartit import PivotDataPool, PivotChart
from django.shortcuts import render_to_response




class RecordsListView(SingleTableView):
    """
    ListView class to receive all Records and pass it to a table
    """
    model = Records
    table_class = RecordsTable
    template_name = 'records_all.html'
    
    

def initial_records(request):
    """
    Load the initial records into the database and calls the list_records page
    """
    IOF.load_file()
    return redirect('list_records')


def create_record(request):
    """
    Render the page to create new records
    """
    form = RecordForm(request.POST or None)

    if form.is_valid():
        form.save()
        return redirect('list_records')
    IOF.write_file()
    return render(request, 'record_form.html', {'form': form})


def update_record(request, id):
    """
    Render the page to update a record
    """
    record = Records.objects.get(id=id)
    form = RecordForm(request.POST or None, instance=record)

    if form.is_valid():
        form.save()
        return redirect('list_records')

    return render(request, 'record_form.html', {'form': form, 'record': record})


def delete_record(request, id):
    """
    Render the page to delete a record
    """
    record = Records.objects.get(id=id)

    if request.method == 'POST':
        record.delete()
        return redirect('list_records')

    return render(request, 'record_delete_confirm.html', {'record': record})


def record_chart_view(request):
    """
    Function to create the chart view 
    Adapted from https://django-chartit.readthedocs.io/en/latest/#
    """
    # Step 1: Create a PivotDataPool with the data we want to retrieve.
    # Pivot Data Pool based on Age
    agepivotdata = PivotDataPool(
        series=[{
            'options': {
                'source': Records.objects.all(),  # the source of graph is the model Records
                'categories': ['age_group'],  # it's grouped by age
                'legend_by': 'student_response',
            },
            'terms': {
                'sum_response': Sum('value'),  # it counts per student response to build the data to be plotted on the chart
            }
        }]
    )
    # Pivot Data Pool based on Gender
    genderpivotdata = PivotDataPool(
        series=[{
            'options': {
                'source': Records.objects.all(),
                'categories': ['sex'],
                'legend_by': 'student_response',
            },
            'terms': {
                'sum_response': Sum('value'),
            }
        }]
    )

    # Step 2: Create the PivotChart object
    # Pivot Chart based on Age
    agepivcht = PivotChart(
        datasource=agepivotdata,  # the pivot data pool created is assigned as the datasource for the chart
        series_options=[{
            'options': {'type': 'column'},
            'terms': ['sum_response']
        }],
        chart_options={
            'title': {'text': 'Students Response by Age'},
            'xAxis': {'title': {'text': 'Age'}},
            'yAxis': {'title': {'text': '# of Answers'}}
        }
    )
    # Pivot Chart based on Gender
    genderpivcht = PivotChart(
        datasource=genderpivotdata,
        series_options=[{
            'options': {'type': 'column'},
            'terms': ['sum_response']
        }],
        chart_options={
            'title': {'text': 'Students Response by Gender'},
            'xAxis': {'title': {'text': 'Gender'}},
            'yAxis': {'title': {'text': '# of Answers'}}
        }
    )

    # Step 3: Send the PivotChart object to the template.
    return render(request, 'charts.html',{'recordpivchart': [agepivcht, genderpivcht]})
