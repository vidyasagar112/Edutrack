# utils/helpers.py

def calculate_percentage(score, total):
    """Calculate percentage from score and total"""
    if total == 0:
        return 0.0
    return round((score / total) * 100, 2)

def get_performance_level(percentage):
    """Get performance level based on percentage"""
    if percentage >= 75:
        return "STRONG"
    elif percentage >= 50:
        return "AVERAGE"
    else:
        return "WEAK"

def get_result(percentage):
    """Get PASS or FAIL based on percentage"""
    return "PASS" if percentage >= 50 else "FAIL"

def safe_average(numbers):
    """Calculate average safely"""
    if not numbers:
        return 0.0
    return round(sum(numbers) / len(numbers), 2)

def validate_required_fields(data, required_fields):
    """Validate that required fields exist in request data"""
    missing = []
    for field in required_fields:
        if field not in data:
            missing.append(field)
    return missing