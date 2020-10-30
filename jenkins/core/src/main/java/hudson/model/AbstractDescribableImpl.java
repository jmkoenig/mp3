/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Kohsuke Kawaguchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.model;

import java.io.File;
import java.io.IOException;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Util;
import hudson.util.IOUtils;
import jenkins.model.Jenkins;

/**
 * Partial default implementation of {@link Describable}.
 *
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractDescribableImpl<T extends AbstractDescribableImpl<T>> implements Describable<T> {

    /**
     * By default looks for a nested class (conventionally named {@code DescriptorImpl}) implementing {@link Descriptor} and marked with {@link Extension}.
     * <p>{@inheritDoc}
     */
    public Descriptor<T> getDescriptor() {
        return Jenkins.getInstance().getDescriptorOrDie(getClass());
    }
    
    static public FilePath tryBuildingFilePath(AbstractBuild<?, ?> build, TaskListener listener, String path)
			throws IOException, InterruptedException {
		EnvVars env = build.getEnvironment(listener);
		String targetPath = Util.replaceMacro(path, build.getBuildVariableResolver());
		targetPath = env.expand(targetPath);

		if (IOUtils.isAbsolute(targetPath)) {
		    return new FilePath(new File(targetPath));
		} else {
		    FilePath mrSettings = build.getModuleRoot().child(targetPath);
		    FilePath wsSettings = build.getWorkspace().child(targetPath);
		    try {
		        if (!wsSettings.exists() && mrSettings.exists()) {
		            wsSettings = mrSettings;
		        }
		    } catch (Exception e) {
		        throw new IllegalStateException("failed to find settings.xml at: " + wsSettings.getRemote());
		    }
		    return wsSettings;
		}
	}

}
