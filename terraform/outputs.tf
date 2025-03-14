output "instance_ip" {
  description = "IP público da instância EC2"
  value       = aws_instance.java_app[0].public_ip
}
