from django.test import TestCase
from .models import Records
from .views import RecordsListView

class RecordsTestCase(TestCase):
    """
    Test case for Records model
    """
    def setUp(self):
        """
        Run before all
        Clean the Records table deleting all onjects in there
        """
        Records.objects.all().delete()
        

    def test_records_create(self):
        """
        Test create a new record on Records objects
        The assertIsNoNone should pass in this test to confirm that a record was created
        """
        print("Mateus Carnevalli Terni - TestCase for create a new RecordListView")
        Records.objects.create(ref_date = 2002,
                                geo = "Canada",
                                dguid = None,
                                sex = "male",
                                age_group = "20 years",
                                student_response = "Often",
                                uom = "percent",
                                uom_id = "239",
                                scalar_factor = "units",
                                scalar_id = "0",
                                vector = "v30413271",
                                coordinate = "1.1.1.1",
                                value = "31",
                                status = None,
                                symbol = None,
                                terminated = None,
                                decimals = 0
                                )
        record = RecordsListView.as_view()
        
        self.assertIsNotNone(record)