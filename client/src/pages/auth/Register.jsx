import { useForm } from "react-hook-form";

export default function Register() {
	const { register, handleSubmit } = useForm();
	const onSubmit = (data) => console.log(data);

	return (
		<div className="flex h-screen items-center justify-center bg-gray-100">
			<form
				onSubmit={handleSubmit(onSubmit)}
				className="bg-white p-6 rounded-xl shadow-md w-80"
			>
				<h2 className="text-2xl font-semibold mb-4 text-center">Register</h2>
				<input
					{...register("email")}
					placeholder="Email"
					className="input mb-3 w-full"
				/>
				<input
					{...register("password")}
					placeholder="Password"
					type="password"
					className="input mb-3 w-full"
				/>
				<button className="btn w-full">Register</button>
			</form>
		</div>
	);
}
