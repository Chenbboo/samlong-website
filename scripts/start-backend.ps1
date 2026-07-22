$projectRoot = Split-Path -Parent $PSScriptRoot
$settings = @{}
Get-Content (Join-Path $projectRoot '.env') | Where-Object { $_ -match '^[A-Z_]+=' } | ForEach-Object {
  $key, $value = $_.Split('=', 2)
  $settings[$key] = $value
}
$env:DB_USERNAME = $settings['MYSQL_USER']
$env:DB_PASSWORD = $settings['MYSQL_PASSWORD']
$env:ADMIN_USERNAME = $settings['ADMIN_USERNAME']
$env:ADMIN_PASSWORD = $settings['ADMIN_PASSWORD']
$env:UPLOAD_DIR = Join-Path $projectRoot 'uploads'
$logs = Join-Path $projectRoot 'logs'
New-Item -ItemType Directory -Force -Path $logs | Out-Null
$process = Start-Process -FilePath 'java' -ArgumentList '-jar','backend\target\samlong-api-1.0.0.jar' -WorkingDirectory $projectRoot -WindowStyle Hidden -RedirectStandardOutput (Join-Path $logs 'backend.out.log') -RedirectStandardError (Join-Path $logs 'backend.err.log') -PassThru
$process.Id
