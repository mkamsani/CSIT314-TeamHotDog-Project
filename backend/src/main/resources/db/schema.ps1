if (Test-Path "C:\Windows\System32\wsl.exe")
{
    wsl.exe -e whoami
    wsl.exe -e pwd
    wsl.exe -e ./schema.sh
}
else
{
    echo "wsl.exe does not exist."
}
