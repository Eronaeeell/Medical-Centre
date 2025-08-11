<div class="form-group">
    <label for="patient">Patient Name</label>
    <input type="text" id="patient" name="name" class="form-control" required>
</div>
<div class="form-group">
    <label for="doctorName">Select Doctor</label>
    <select class="form-control" id="doctorName" name="doctorName" required>
        <option value="Dr. Smith">Dr. Smith (Cardiology)</option>
        <option value="Dr. Johnson">Dr. Johnson (General Medicine)</option>
        <option value="Dr. Steve">Dr. Strange (Neurosurgery)</option>
        <option value="Dr. Lee">Dr. Lee (Orthopaedic)</option>
    </select>
</div>
<div class="form-group">
    <label for="date">Date</label>
    <input type="date" id="date" name="date" class="form-control" required>
</div>
<div class="form-group">
    <label for="time">Time</label>
    <input type="time" id="time" name="time" class="form-control" required>
</div>  
<div class="form-group">
    <label for="type">Type of Appointment</label>
    <select class="form-control" id="type" name="type" required>
        <option value="Consultation">Consultation</option>
        <option value="Follow-up">Follow-up</option>
    </select>
</div>
<div class="form-group">
    <label for="status">Status</label>
    <select id="status" name="status" class="form-control" required>
        <option value="confirmed">Confirmed</option>
        <option value="pending">Pending</option>
        <option value="completed">Completed</option>
        <option value="cancelled">Cancelled</option>
    </select>
</div>
<div class="form-group">
    <label for="reason">Notes</label>
    <textarea id="notes" name="reason" class="form-control"></textarea>
</div>
