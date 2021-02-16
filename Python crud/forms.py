from django import forms
from .models import Records


class RecordForm(forms.ModelForm):
    
    class Meta:
        model = Records
        fields = ['ref_date', 'geo', 'dguid', 'sex', 'age_group', 'student_response', 'value']
        
        