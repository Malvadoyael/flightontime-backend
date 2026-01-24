
import os

file_path = r"C:\Users\Eduardo1\Downloads\flightontime-backend\src\main\resources\mes_retrasos_por_aerolinea.json"
temp_file_path = file_path + ".tmp"

try:
    with open(file_path, 'r', encoding='utf-8') as f_in, open(temp_file_path, 'w', encoding='utf-8') as f_out:
        f_out.write('[')
        # Copy content
        for chunk in iter(lambda: f_in.read(1024*1024), ''):
            f_out.write(chunk)
        f_out.write(']')
    
    # Replace original file
    os.replace(temp_file_path, file_path)
    print("Successfully fixed JSON file.")

except Exception as e:
    print(f"Error: {e}")
    if os.path.exists(temp_file_path):
        os.remove(temp_file_path)
