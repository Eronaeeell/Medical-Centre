<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Bootstrap modal: Profile -->
<div class="modal fade" id="profileModal" tabindex="-1" role="dialog" aria-labelledby="profileTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="profileTitle">My Profile</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
      </div>

      <form action="CustomerProfileServlet" method="post" class="mb-0">
        <input type="hidden" name="action" value="updateProfile"/>
        <input type="hidden" name="id" value="${sessionScope.user.id}"/>

        <div class="modal-body p-0">
          <table class="table table-borderless mb-0">
            <tbody>
              <tr>
                <th scope="row" class="w-25">Name</th>
                <td>
                  <c:out value="${sessionScope.user.name}" default="-" />
                </td>
              </tr>

              <tr>
                <th scope="row">Age</th>
                <td>
                  <!-- If your Customer doesn't have 'age', this will render blank; replace with your field or compute from DOB -->
                  <c:choose>
                    <c:when test="${not empty sessionScope.user.age}">
                      <c:out value="${sessionScope.user.age}" />
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
              </tr>
              
              <tr>
                <th scope="row">IC</th>
                <td>
                  <!-- Change 'user.ic' to 'user.icNo' or 'user.nric' if that matches your entity -->
                  <c:out value="${sessionScope.user.ic}" default="-" />
                </td>
              </tr>

              <tr>

              <tr>
                <th scope="row">Email</th>
                <td>
                  <input type="email" name="email" class="form-control form-control-sm"
                         value="${sessionScope.user.email}" placeholder="Enter email" />
                </td>
              </tr>

              <tr>
                <th scope="row">Phone Number</th>
                <td>
                  <input type="text" name="phone" class="form-control form-control-sm"
                         value="${sessionScope.user.phoneNo}" placeholder="Enter phone number" />
                </td>
              </tr>

              <tr>
                <th scope="row">Password</th>
                <td>
                  <div class="input-group input-group-sm">
                    <input type="password" id="profilePassword" class="form-control" readonly
                           value="${sessionScope.user.password}" />
                    <div class="input-group-append">
                      <button class="btn btn-outline-secondary" type="button" id="togglePwBtn">Show</button>
                    </div>
                  </div>
                  <small class="text-muted d-block mt-1">Password is hidden. Click “Show” to reveal.</small>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="modal-footer py-2">
          <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Close</button>
          <button type="submit" class="btn btn-primary btn-sm">Save Changes</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Tiny script to toggle password visibility -->
<script>
(function () {
  var btn = document.getElementById('togglePwBtn');
  var input = document.getElementById('profilePassword');
  if (!btn || !input) return;
  btn.addEventListener('click', function () {
    if (input.type === 'password') { input.type = 'text'; btn.textContent = 'Hide'; }
    else { input.type = 'password'; btn.textContent = 'Show'; }
  });
})();
</script>
