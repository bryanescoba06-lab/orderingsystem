#!/usr/bin/env python3
"""
Connect and authenticate with the Ordering System at localhost:8081
Usage:
  python connect_login.py                           # Interactive mode
  python connect_login.py --username user --pass pwd  # Direct login
  python connect_login.py --register --username user --pass pwd --role ADMIN
"""

import requests
import json
import sys
import argparse
import os

BASE_URL = "http://localhost:8081"
TOKEN_FILE = ".ordering_system_token"

def save_token(token, username, role):
    """Save token to file for later use"""
    data = {"token": token, "username": username, "role": role}
    with open(TOKEN_FILE, 'w') as f:
        json.dump(data, f)
    print(f"✓ Token saved to {TOKEN_FILE}")

def load_token():
    """Load saved token"""
    if os.path.exists(TOKEN_FILE):
        with open(TOKEN_FILE, 'r') as f:
            return json.load(f)
    return None

def register(username, password, role="STAFF"):
    """Register a new user"""
    url = f"{BASE_URL}/api/auth/register"
    payload = {"username": username, "password": password, "role": role}
    try:
        response = requests.post(url, json=payload)
        if response.status_code == 200:
            print(f"✓ User '{username}' registered with role '{role}'")
            return True
        else:
            error = response.json().get('message', 'Unknown error')
            print(f"✗ Registration failed: {error}")
            return False
    except requests.exceptions.ConnectionError:
        print(f"✗ Cannot connect to {BASE_URL}. Is the server running?")
        sys.exit(1)

def login(username, password):
    """Login and get JWT token"""
    url = f"{BASE_URL}/api/auth/login"
    payload = {"username": username, "password": password}
    try:
        response = requests.post(url, json=payload)
        if response.status_code == 200:
            data = response.json()
            print(f"✓ Login successful!")
            print(f"  Username: {data['username']}")
            print(f"  Role: {data['role']}")
            print(f"  Token: {data['token'][:60]}...")
            return data
        else:
            print(f"✗ Login failed: Invalid credentials (HTTP {response.status_code})")
            return None
    except requests.exceptions.ConnectionError:
        print(f"✗ Cannot connect to {BASE_URL}. Is the server running?")
        sys.exit(1)

def test_token(token):
    """Test token by accessing dashboard"""
    url = f"{BASE_URL}/dashboard"
    headers = {"Authorization": f"Bearer {token}"}
    try:
        response = requests.get(url, headers=headers)
        if response.status_code == 200:
            print(f"✓ Token is valid - can access protected endpoints")
            return True
        else:
            print(f"✗ Token validation failed: HTTP {response.status_code}")
            return False
    except Exception as e:
        print(f"✗ Error testing token: {e}")
        return False

def main():
    parser = argparse.ArgumentParser(description="Connect and authenticate with Ordering System")
    parser.add_argument("--username", "-u", help="Username")
    parser.add_argument("--password", "-p", help="Password")
    parser.add_argument("--role", "-r", default="STAFF", help="Role for registration (ADMIN, STAFF, etc.)")
    parser.add_argument("--register", "-R", action="store_true", help="Register new user instead of login")
    parser.add_argument("--test-only", "-t", action="store_true", help="Only test existing saved token")
    parser.add_argument("--logout", "-L", action="store_true", help="Clear saved token")
    args = parser.parse_args()

    # Logout
    if args.logout:
        if os.path.exists(TOKEN_FILE):
            os.remove(TOKEN_FILE)
            print("✓ Logged out - token cleared")
        else:
            print("No saved token found")
        return

    # Test only
    if args.test_only:
        saved = load_token()
        if saved:
            if test_token(saved['token']):
                print(f"Current user: {saved['username']} ({saved['role']})")
        else:
            print("No saved token found. Login first.")
        return

    # Get credentials
    username = args.username
    password = args.password
    if not username or not password:
        # Interactive mode
        print("=== Ordering System Authentication ===")
        username = input("Username: ").strip()
        password = input("Password: ").strip()
        if not username or not password:
            print("Username and password required")
            sys.exit(1)

    # Check if we should register
    if args.register:
        if not register(username, password, args.role):
            sys.exit(1)
        print(f"\nNow you can login with these credentials:")
        input("Press Enter to continue...")

    # Login
    result = login(username, password)
    if result:
        save_token(result['token'], result['username'], result['role'])
        print()
        test_token(result['token'])

if __name__ == "__main__":
    main()
