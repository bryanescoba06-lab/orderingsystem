// Decode JWT token
function parseJwt(token) {
    try {
        const base64 = token.split('.')[1];
        const decoded = JSON.parse(atob(base64));
        return decoded;
    } catch (e) {
        return null;
    }
}

// Get current user info
function getCurrentUser() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    return parseJwt(token);
}

// Get role from token — strips ROLE_ prefix if present
function getUserRole() {
    const user = getCurrentUser();
    if (!user) return null;

    // Check both 'role' and 'roles' claim
    let role = user.role || user.roles || user.authorities || '';

    // If it's an array, take first element
    if (Array.isArray(role)) role = role[0];

    // Strip ROLE_ prefix
    return role.toString().replace('ROLE_', '').toUpperCase().trim();
}

// Check if user is logged in
function requireLogin() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login';
        return false;
    }
    return true;
}

// Check if user has required role
function requireRole(allowedRoles) {
    if (!requireLogin()) return false;
    const role = getUserRole();
    console.log('Current role:', role);
    console.log('Allowed roles:', allowedRoles);
    if (!allowedRoles.includes(role)) {
        alert('Access Denied! Your role (' + role +
              ') cannot access this page.');
        window.location.href = '/dashboard';
        return false;
    }
    return true;
}

// Logout
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
}

/**
 * 
 */