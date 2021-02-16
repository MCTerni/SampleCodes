"""
Model for cst8333 project.
Author: Mateus Carnevalli Terni
Version: 20200303

Reference for customized Tables:
    'django-tables2 Tutorial'
    [Online]. Available: https://django-tables2.readthedocs.io/en/latest/pages/tutorial.html
    [Accessed: 19-Mar-2020]

"""
import django_tables2 as tables
from .models import Records

class RecordsTable(tables.Table):

    #https://stackoverflow.com/questions/44911679/putting-a-click-event-for-a-dialogue-box-with-django-tables2
    template_code = '''  
                    <a href="{% url 'update_record' record.id %}">Update</a>
                    <a href="{% url 'delete_record' record.id %}">Delete</a>'''
    change = tables.TemplateColumn(template_code,  
                                    verbose_name=u'Change', )

    class Meta:
        model = Records
        template_name = "django_tables2/bootstrap.html"
        fields = ['ref_date', 'geo', 'sex', 
                'age_group', 'student_response', 'value'
                ]
        