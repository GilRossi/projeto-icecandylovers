output "instance_ip" {
  description = "IP público da instância EC2"
  value       = aws_instance.spring_app.public_ip
}