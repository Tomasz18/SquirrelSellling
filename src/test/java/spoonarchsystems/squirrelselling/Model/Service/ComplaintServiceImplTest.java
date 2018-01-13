package spoonarchsystems.squirrelselling.Model.Service;

import org.hibernate.HibernateException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;
import spoonarchsystems.squirrelselling.Model.DAO.ComplaintDAO;
import spoonarchsystems.squirrelselling.Model.Entity.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ComplaintServiceImplTest {

    private Order prepareOrder() {
        Order order = new Order();
        ArrayList<OrderPosition> orderPositions = new ArrayList<>();
        OrderPosition orderPosition1 = new OrderPosition();
        orderPosition1.setNumber(1);
        orderPosition1.setQuantity(102.56);
        Ware ware1 = new Ware();
        ware1.setId(1);
        orderPosition1.setWare(ware1);
        OrderPosition orderPosition2 = new OrderPosition();
        orderPosition2.setNumber(2);
        orderPosition2.setQuantity(2.0);
        Ware ware2 = new Ware();
        ware2.setId(2);
        orderPosition2.setWare(ware2);
        OrderPosition orderPosition3 = new OrderPosition();
        orderPosition3.setNumber(3);
        orderPosition3.setQuantity(6.5);
        Ware ware3 = new Ware();
        ware3.setId(3);
        orderPosition3.setWare(ware3);
        orderPositions.add(orderPosition1);
        orderPositions.add(orderPosition2);
        orderPositions.add(orderPosition3);
        order.setPositions(orderPositions);

        return order;
    }

    private void setComplaintDAO(ComplaintServiceImpl complaintService, ComplaintDAO complaintDAO) {
        Field complaintDAOField = ReflectionUtils.findField(ComplaintServiceImpl.class, "complaintDAO");
        complaintDAOField.setAccessible(true);
        ReflectionUtils.setField(complaintDAOField, complaintService, complaintDAO);
    }

    private void setCurrentComplaint(ComplaintServiceImpl complaintService, Complaint currentComplaint) {
        Field complaintField = ReflectionUtils.findField(complaintService.getClass(), "complaint");
        complaintField.setAccessible(true);
        ReflectionUtils.setField(complaintField, complaintService, currentComplaint);
    }

    private String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
       return dateFormat.format(calendar.getTime());
    }

    @Test
    public void saveComplaintSaveFailure() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Complaint complaint = new Complaint();
        complaint.setDescription("Description");
        Complaint currentComplaint = new Complaint();

        Complaint databaseComplaint = new Complaint();
        databaseComplaint.setNumber("0001/" + getToday());
        ArrayList<Complaint> complaints = new ArrayList<>();
        complaints.add(databaseComplaint);

        ComplaintDAO complaintDAOMock = Mockito.mock(ComplaintDAO.class);
        Mockito.when(complaintDAOMock.getComplaintsBySubmissionDate(Mockito.isA(Date.class))).thenReturn(complaints);
        Mockito.doThrow(new HibernateException("")).when(complaintDAOMock).saveComplaint(Mockito.isA(Complaint.class));
        setComplaintDAO(complaintService, complaintDAOMock);
        setCurrentComplaint(complaintService, currentComplaint);

        boolean status = complaintService.saveComplaint(complaint);

        assertFalse(status);
    }

    @Test
    public void saveComplaintToBigNumber() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Complaint complaint = new Complaint();
        complaint.setNumber("9999/" + getToday());
        ArrayList<Complaint> complaints = new ArrayList<>();
        complaints.add(complaint);

        ComplaintDAO complaintDAOMock = Mockito.mock(ComplaintDAO.class);
        Mockito.when(complaintDAOMock.getComplaintsBySubmissionDate(Mockito.isA(Date.class))).thenReturn(complaints);
        setComplaintDAO(complaintService, complaintDAOMock);

        boolean status = complaintService.saveComplaint(new Complaint());

        assertFalse(status);
    }

    @Test
    public void saveComplaintNoErrors() throws Exception {
        ArrayList<Complaint> complaints = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Complaint complaint1 = new Complaint();
        complaint1.setNumber("0001/" + getToday());
        Complaint complaint2 = new Complaint();
        complaint2.setNumber("0002/" + getToday());
        complaints.add(complaint1);
        complaints.add(complaint2);

        Complaint complaint = new Complaint();
        complaint.setDescription("Description");
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        ComplaintDAO complaintDAOMock = Mockito.mock(ComplaintDAO.class);
        Mockito.when(complaintDAOMock.getComplaintsBySubmissionDate(Mockito.isA(Date.class))).thenReturn(complaints);

        setComplaintDAO(complaintService, complaintDAOMock);
        setCurrentComplaint(complaintService, new Complaint());

        boolean status = complaintService.saveComplaint(complaint);

        ArgumentCaptor<Complaint> complaintArgument = ArgumentCaptor.forClass(Complaint.class);
        Mockito.verify(complaintDAOMock).saveComplaint(complaintArgument.capture());

        assertEquals(Complaint.ComplaintStatus.submitted, complaintArgument.getValue().getStatus());
        assertEquals("0003/" + getToday(), complaintArgument.getValue().getNumber());
        assertEquals(complaint.getDescription(), complaintArgument.getValue().getDescription());
        assertEquals(getToday(), dateFormat.format(complaintArgument.getValue().getSubmissionDate()));
        assertTrue(status);
    }

    @Test
    public void prepareComplaint() throws Exception {
        Order order = prepareOrder();
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();

        Complaint complaint = new Complaint();
        ArrayList<ComplaintPosition> complaintPositions = new ArrayList<>();
        ComplaintPosition complaintPosition1 = new ComplaintPosition();
        complaintPosition1.setNumber(1);
        complaintPosition1.setQuantity(10.0);
        ComplaintPosition complaintPosition2 = new ComplaintPosition();
        complaintPosition2.setNumber(null);
        complaintPosition2.setQuantity(6.5);
        ComplaintPosition complaintPosition3 = new ComplaintPosition();
        complaintPosition3.setNumber(3);
        complaintPosition3.setQuantity(5.0);
        complaintPositions.add(complaintPosition1);
        complaintPositions.add(complaintPosition2);
        complaintPositions.add(complaintPosition3);
        complaint.setPositions(complaintPositions);

        Complaint preparedComplaint = complaintService.prepareComplaint(complaint, order);

        assertEquals(preparedComplaint, complaintService.getCurrentComplaint());
        assertEquals(new Integer(1), preparedComplaint.getPositions().get(0).getNumber());
        assertEquals(new Double(10.0), preparedComplaint.getPositions().get(0).getQuantity());
        assertEquals(1, preparedComplaint.getPositions().get(0).getWare().getId());
        assertEquals(preparedComplaint, preparedComplaint.getPositions().get(0).getComplaint());
        assertEquals(new Integer(2), preparedComplaint.getPositions().get(1).getNumber());
        assertEquals(new Double(5.0), preparedComplaint.getPositions().get(1).getQuantity());
        assertEquals(3, preparedComplaint.getPositions().get(1).getWare().getId());
        assertEquals(preparedComplaint, preparedComplaint.getPositions().get(1).getComplaint());
    }

    @Test
    public void validateComplaintNoErrors() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Order order = prepareOrder();

        Complaint complaintForm = new Complaint();
        ArrayList<ComplaintPosition> complaintPositions = new ArrayList<>();
        ComplaintPosition complaintPosition1 = new ComplaintPosition();
        complaintPosition1.setNumber(1);
        complaintPosition1.setQuantity(0.1);
        ComplaintPosition complaintPosition2 = new ComplaintPosition();
        complaintPosition2.setNumber(3);
        complaintPosition2.setQuantity(6.5);
        complaintPositions.add(complaintPosition1);
        complaintPositions.add(complaintPosition2);
        complaintForm.setPositions(complaintPositions);

        HashSet<Integer> errors = (HashSet<Integer>) complaintService.validateComplaint(complaintForm, order);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void validateComplaintTooHighQuantity() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Order order = prepareOrder();

        Complaint complaintForm = new Complaint();
        ArrayList<ComplaintPosition> complaintPositions = new ArrayList<>();
        ComplaintPosition complaintPosition1 = new ComplaintPosition();
        complaintPosition1.setNumber(3);
        complaintPosition1.setQuantity(7.0);
        complaintPositions.add(complaintPosition1);
        complaintForm.setPositions(complaintPositions);

        HashSet<Integer> errors = (HashSet<Integer>) complaintService.validateComplaint(complaintForm, order);

        assertTrue(errors.contains(ComplaintServiceImpl.BAD_AMOUNT));
    }

    @Test
    public void validateComplaintTooLowQuantity() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Order order = prepareOrder();

        Complaint complaintForm = new Complaint();
        ArrayList<ComplaintPosition> complaintPositions = new ArrayList<>();
        ComplaintPosition complaintPosition1 = new ComplaintPosition();
        complaintPosition1.setNumber(1);
        complaintPosition1.setQuantity(0.0);
        complaintPositions.add(complaintPosition1);
        complaintForm.setPositions(complaintPositions);

        HashSet<Integer> errors = (HashSet<Integer>) complaintService.validateComplaint(complaintForm, order);

        assertTrue(errors.contains(ComplaintServiceImpl.BAD_AMOUNT));
    }

    @Test
    public void validateComplaintNoPositions() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Order order = prepareOrder();

        Complaint complaintForm = new Complaint();
        complaintForm.setPositions(new ArrayList<>());

        HashSet<Integer> errors = (HashSet<Integer>) complaintService.validateComplaint(complaintForm, order);

        assertTrue(errors.contains(ComplaintServiceImpl.BAD_AMOUNT));
    }

    @Test
    public void validateComplaintInvalidPosition() throws Exception {
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        Order order = prepareOrder();

        Complaint complaintForm = new Complaint();
        ArrayList<ComplaintPosition> complaintPositions = new ArrayList<>();
        ComplaintPosition complaintPosition1 = new ComplaintPosition();
        complaintPosition1.setNumber(1);
        complaintPosition1.setQuantity(0.1);
        ComplaintPosition complaintPosition2 = new ComplaintPosition();
        complaintPosition2.setNumber(4);
        complaintPosition2.setQuantity(125.01);
        complaintPositions.add(complaintPosition1);
        complaintPositions.add(complaintPosition2);
        complaintForm.setPositions(complaintPositions);

        HashSet<Integer> errors = (HashSet<Integer>) complaintService.validateComplaint(complaintForm, order);

        assertTrue(errors.contains(ComplaintServiceImpl.INVALID_POSITION));
    }

    @Test
    public void getComplaintForm() throws Exception {
        String orderNumber = "0001/2018-01-22";
        Order order = prepareOrder();
        order.getPositions().remove(2);
        order.setNumber(orderNumber);
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();

        Complaint complaintForm = complaintService.getComplaintForm(order);

        assertNotNull(complaintForm.getPositions());
        assertEquals(order, complaintForm.getOrder());
        assertEquals(orderNumber, complaintForm.getOrder().getNumber());
        assertEquals(new Integer(1), complaintForm.getPositions().get(0).getNumber());
        assertEquals(new Integer(2), complaintForm.getPositions().get(1).getNumber());
        assertEquals(new Double(102.56), complaintForm.getPositions().get(0).getQuantity());
        assertEquals(new Double(2.0), complaintForm.getPositions().get(1).getQuantity());
    }

    @Test
    public void getCurrentComplaint() throws Exception {
        String currentComplaintNumber = "0001/2018-01-11";
        Complaint currentComplaint = new Complaint();
        currentComplaint.setNumber(currentComplaintNumber);
        ComplaintServiceImpl complaintService = new ComplaintServiceImpl();
        setCurrentComplaint(complaintService, currentComplaint);

        String complaintNumber = complaintService.getCurrentComplaint().getNumber();

        assertEquals(currentComplaintNumber, complaintNumber);
    }

}